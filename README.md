# Knowledge Assistant

An AI-powered document question-answering web application that allows users to upload PDF or TXT documents and ask questions about their content.

## Live Demo

**Live Application:** https://knowledge-assistant-4we9.onrender.com

**GitHub Repository:** https://github.com/jagadeesh17409/knowledge-assistant

## Features

- Upload text-based PDF and TXT documents.
- Supports files up to 5 MB.
- Extracts text from uploaded documents.
- Accepts natural-language questions about the uploaded content.
- Uses the Groq API and a language model to generate answers.
- Instructs the AI to answer using the supplied document text and indicate when information is not found.
- Provides upload and question-answering status messages.
- Includes basic validation and error handling.
- Responsive web interface.
- Deployable as a Spring Boot application.

## Technology Stack

### Frontend

- HTML5
- CSS3
- JavaScript
- Bootstrap

### Backend

- Java
- Spring Boot
- Spring MVC / REST API
- Maven

### AI Integration

- Groq API
- OpenAI-compatible chat completions endpoint

### Deployment and Version Control

- Docker
- Render
- Git and GitHub

## Architecture

The application follows a simple client-server architecture.

1. The user opens the web application in a browser.
2. The frontend sends the selected PDF or TXT file to the Spring Boot backend.
3. The backend extracts and processes the document text.
4. The user submits a question about the uploaded document.
5. The backend sends the question and relevant document text to the Groq API.
6. The AI generates a response based on the supplied text.
7. The backend returns the answer to the frontend for display.

## Project Structure

```text
knowledge-assistant/
├── Dockerfile
├── pom.xml
├── README.md
├── .gitignore
├── company-policy.txt
└── src/
    └── main/
        ├── java/
        │   └── com/jagadeesh/knowledgeassistant/
        │       ├── AiService.java
        │       ├── DocumentService.java
        │       ├── KnowledgeAssistantApplication.java
        │       └── KnowledgeController.java
        └── resources/
            ├── application.properties
            └── static/
                ├── index.html
                ├── style.css
                └── app.js
```

## Prerequisites

Install the following before running the project locally:

- Java 17 or a compatible version matching the project configuration
- Maven
- Git
- A Groq API key

## Configuration

The application reads the Groq API key from an environment variable.

The relevant entry in `application.properties` is:

```properties
groq.api.key=${GROQ_API_KEY:}
server.port=${PORT:8080}
```

Set your API key in the environment. Do not hardcode it in the source code or commit it to GitHub.

### Windows Command Prompt

```cmd
set GROQ_API_KEY=YOUR_GROQ_API_KEY
```

Replace `YOUR_GROQ_API_KEY` with your own key. Run the application from the same terminal session.

## Run Locally

1. Clone the repository:

```bash
git clone https://github.com/jagadeesh17409/knowledge-assistant.git
```

2. Open the project directory:

```bash
cd knowledge-assistant
```

3. Set the `GROQ_API_KEY` environment variable as described above.

4. Build the application:

```bash
mvn clean package
```

5. Start the Spring Boot application:

```bash
mvn spring-boot:run
```

6. Open the application in your browser:

```text
http://localhost:8080
```

## API Endpoints

The application exposes the following functional endpoints:

| Endpoint      | Method | Purpose                                       |
| ------------- | ------ | --------------------------------------------- |
| `/api/upload` | POST   | Upload a document for processing              |
| `/api/ask`    | POST   | Submit a question about the uploaded document |

The upload endpoint accepts a multipart file using the field name `file`. The question endpoint expects the request format implemented by the frontend and backend.

## Error Handling

The application includes checks for:

- Missing file selection
- Unsupported file extensions
- Files exceeding the configured size limit
- Empty questions
- Questions submitted before uploading a document
- Missing Groq API configuration
- Errors returned by the Groq API

## Deployment

The application is deployed on Render using Docker.

**Live URL:** https://knowledge-assistant-4we9.onrender.com

The deployment uses an environment variable named `GROQ_API_KEY` to provide the API key securely.

## Limitations

- Uploaded document content is stored in application memory for the running instance rather than in a persistent database.
- Document content may need to be uploaded again after a restart or redeployment.
- The application is designed as a simple document question-answering demo.
- Answer quality depends on the extracted document text and the AI model response.
- PDF support depends on the document-processing implementation; scanned PDFs may require OCR if that capability is not implemented.

## Future Improvements

- Add persistent document storage.
- Improve retrieval for large documents using chunking and embeddings.
- Add automated unit and integration tests.
- Support OCR for scanned PDFs.
- Add authentication and user-specific document management.

## Security

- Keep API keys in environment variables.
- Never commit secrets or private credentials to GitHub.
- Upload only documents you are authorized to process.
- Avoid uploading confidential or sensitive information to a public demo.

## Author

**Jagadeesh Dangeti**

B.Tech in Computer Science and Engineering (2025)

GitHub: https://github.com/jagadeesh17409
