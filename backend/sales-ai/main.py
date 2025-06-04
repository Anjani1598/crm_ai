from fastapi import FastAPI, HTTPException
from pydantic import BaseModel, EmailStr
import smtplib
from email.mime.text import MIMEText
from email.mime.multipart import MIMEMultipart
from typing import Optional, List
import os
from dotenv import load_dotenv
from google import genai
from fastapi.middleware.cors import CORSMiddleware

# Load environment variables
load_dotenv()

# Get API keys
# Removed: openai.api_key = "YOUR_OPENAI_API_KEY"
GOOGLE_API_KEY = os.getenv("GOOGLE_API_KEY")

if not GOOGLE_API_KEY:
    raise Exception("GOOGLE_API_KEY environment variable not set.")

client = genai.Client(api_key=GOOGLE_API_KEY)

app = FastAPI()

# Add CORS middleware
app.add_middleware(
    CORSMiddleware,
    allow_origins=["http://localhost:8080"],  # Frontend URL
    allow_credentials=True,
    allow_methods=["*"],  # Allows all methods
    allow_headers=["*"],  # Allows all headers
)

class CredentialsEmailRequest(BaseModel):
    customer_name: str
    customer_email: EmailStr
    username: str
    password: str
    product_name: str = "Our Platform"  # Default value
    login_url: str
    support_email: str
    help_center_url: str
    company_name: str

def create_credentials_email_template(
    customer_name: str,
    username: str,
    password: str,
    product_name: str,
    login_url: str,
    support_email: str,
    help_center_url: str,
    company_name: str
) -> str:
    template = f"""
Subject: Your Login Credentials for {product_name}!

Hi {customer_name},

Welcome aboard! We're thrilled to have you as part of the {product_name} family.

Here are your login details to get started:

**Username:** `{username}`
**Password:** `{password}`

You can log in directly here: {login_url}

We recommend logging in as soon as possible to change your password to something more memorable and secure.

If you have any questions or need assistance, don't hesitate to reach out to our support team at {support_email} or visit our help center at {help_center_url}.

We're excited for you to explore {product_name}!

Best regards,

The {company_name} Team
"""
    return template

def send_email(to_email: str, subject: str, body: str, from_email: str = None):
    try:
        # Get email credentials from environment variables
        smtp_server = os.getenv("SMTP_SERVER", "smtp.gmail.com")
        smtp_port = int(os.getenv("SMTP_PORT", "587"))
        smtp_username = os.getenv("SMTP_USERNAME")
        smtp_password = os.getenv("SMTP_PASSWORD")
        
        if not all([smtp_username, smtp_password]):
            raise HTTPException(
                status_code=500,
                detail="Email configuration is missing. Please set SMTP_USERNAME and SMTP_PASSWORD environment variables."
            )

        # Use provided from_email or default to SMTP username
        from_email = from_email or smtp_username

        # Create message
        message = MIMEMultipart()
        message["From"] = from_email
        message["To"] = to_email
        message["Subject"] = subject

        # Add body to email
        message.attach(MIMEText(body, "plain"))

        # Create SMTP session
        with smtplib.SMTP(smtp_server, smtp_port) as server:
            server.starttls()
            server.login(smtp_username, smtp_password)
            server.send_message(message)
        
        return True
    except smtplib.SMTPAuthenticationError:
        raise HTTPException(
            status_code=500,
            detail="Email authentication failed. Please check your SMTP credentials."
        )
    except Exception as e:
        raise HTTPException(
            status_code=500,
            detail=f"Failed to send email: {str(e)}"
        )

@app.get("/")
def read_root():
    return {"message": "Hello World"}

@app.post("/send-credentials-email")
async def send_credentials_email(request: CredentialsEmailRequest):
    try:
        # Create email body using template
        email_body = create_credentials_email_template(
            customer_name=request.customer_name,
            username=request.username,
            password=request.password,
            product_name=request.product_name,
            login_url=request.login_url,
            support_email=request.support_email,
            help_center_url=request.help_center_url,
            company_name=request.company_name
        )

        # Send the email
        send_email(
            to_email=request.customer_email,
            subject=f"Your Login Credentials for {request.product_name}!",
            body=email_body
        )
        return {"message": "Credentials email sent successfully"}
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

# Removed the old generate_deal_coaching function
# Re-implementing generate_deal_coaching using Gemini
def generate_deal_coaching(deal_data, chat_history):
    prompt = f"""
You are a sales assistant AI (ROLE_ADMIN). Based on the following deal information and chat history, suggest next steps to improve the close probability.

Deal Info:
- Deal Name: {deal_data['deal_name']}
- Amount: {deal_data['amount']}
- Current Stage: {deal_data['stage']}
- Expected Close Date: {deal_data['close_date']}

Chat History (format: [Speaker]: Message):
{chat_history}

Next Steps (as ROLE_ADMIN):
"""

    try:
        response = client.models.generate_content(
            model='gemini-2.0-flash',
            contents=prompt,
        )
        return response.text
    except Exception as e:
        print(f"Error generating content: {e}")
        return "Could not generate coaching suggestions at this time."

class DealRequest(BaseModel):
    deal_name: str
    amount: str
    stage: str
    close_date: str
    chat_history: str

@app.post("/deal-coach/")
def deal_coach(request: DealRequest):
    suggestion = generate_deal_coaching(request.dict(), request.chat_history)
    return {"next_steps": suggestion}

# === Pydantic Models ===
class AgentRequest(BaseModel):
    client_message: str
    conversation_history: Optional[List[dict]] = None

class ClientRequest(BaseModel):
    company_type: str
    needs: List[str]
    user_count: int
    agent_message: Optional[str] = None
    conversation_history: Optional[List[dict]] = None

class MoodToneRequest(BaseModel):
    message: str

@app.post("/analyze-mood-tone/")
def analyze_mood_tone(request: MoodToneRequest):
    analysis_prompt = f"""
Analyze the following message and provide:
1. The mood (e.g., professional, enthusiastic, concerned, neutral)
2. The tone (e.g., formal, casual, assertive, empathetic)

Message to analyze: "{request.message}"

Provide the analysis in two separate lines:
Mood: [mood]
Tone: [tone]
"""

    try:
        analysis = client.models.generate_content(
            model='gemini-2.0-flash',
            contents=analysis_prompt,
        )
        
        # Parse the response to separate mood and tone
        analysis_text = analysis.text.strip()
        mood = ""
        tone = ""
        
        for line in analysis_text.split('\n'):
            if line.startswith('Mood:'):
                mood = line.replace('Mood:', '').strip()
            elif line.startswith('Tone:'):
                tone = line.replace('Tone:', '').strip()
        
        return {
            "mood": mood,
            "tone": tone
        }
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

# === CLIENT AI ENDPOINT ===
@app.post("/client-ai/")
def client_ai(request: ClientRequest):
    needs_text = ", ".join(request.needs)
    
    # Format conversation history if it exists
    conversation_context = ""
    if request.conversation_history:
        conversation_context = "\nPrevious conversation:\n"
        for msg in request.conversation_history:
            speaker = msg.get("speaker", "Unknown")
            message = msg.get("message", "")
            conversation_context += f"[{speaker}]: {message}\n"
    
    if request.agent_message:
        # If there's an agent message, generate a response to it
        prompt = f"""
You are an AI assistant representing a {request.company_type} company. A CRM solution provider (ROLE_ADMIN) sent: "{request.agent_message}"
{conversation_context}
Generate a ONE to TWO LINE professional response as ROLE_CUSTOMER, considering that your company needs: {needs_text} and support for {request.user_count} users.
Keep it extremely concise and business-like, and maintain consistency with previous conversations.
"""
    else:
        # Original prompt for initial message
        prompt = f"""
You are an AI assistant representing a {request.company_type} company. Generate a ONE to TWO LINE professional message to a CRM solution provider as ROLE_CUSTOMER.
{conversation_context}
Mention that the company needs: {needs_text} and support for {request.user_count} users.
Keep it extremely concise and business-like.
"""

    try:
        response = client.models.generate_content(
            model='gemini-2.0-flash',
            contents=prompt,
        )
        return {"client_message": response.text}
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

# === AGENT AI ENDPOINT ===
@app.post("/agent-ai/")
def agent_ai(request: AgentRequest):
    # Format conversation history if it exists
    conversation_context = ""
    if request.conversation_history:
        conversation_context = "\nPrevious conversation:\n"
        for msg in request.conversation_history:
            speaker = msg.get("speaker", "Unknown")
            message = msg.get("message", "")
            conversation_context += f"[{speaker}]: {message}\n"

    prompt = f"""
You are a CRM company sales engineer (ROLE_ADMIN). A client (ROLE_CUSTOMER) sent: "{request.client_message}"
{conversation_context}
Respond with ONE to TWO LINES maximum as ROLE_ADMIN, including a brief acknowledgment and next step.
Keep it extremely concise and professional.
"""

    try:
        response = client.models.generate_content(
            model='gemini-2.0-flash',
            contents=prompt,
        )
        return {"agent_response": response.text}
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))
