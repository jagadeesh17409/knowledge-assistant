# Knowledge Assistant (Fresher-Friendly)

A small document Q&A web app built with HTML, CSS, JavaScript, Bootstrap, Java, and Spring Boot.

## Features
- Upload a text-based PDF or TXT file (maximum 5 MB).
- Extract text from the document.
- Split document text into smaller chunks.
- Retrieve up to three relevant chunks using simple keyword matching.
- Send the retrieved text and question to Gemini for an AI-generated answer.
- Basic upload/question validation and readable error messages.
- Responsive Bootstrap UI.

## Requirements
- Java 17 or later
- Maven 3.8+ (or use the Maven wrapper if you add one)
- A Gemini API key for AI answers
- Internet connection for the AI API and Bootstrap CDN

## Run locally
1. Install Java 17 and Maven.
2. Create a Gemini API key in Google AI Studio: https://aistudio.google.com/app/apikey
3. Set the API key as an environment variable. Do not paste it into frontend JavaScript or commit it to GitHub.
   - Windows PowerShell: `$env:GEMINI_API_KEY="YOUR_REAL_KEY"`
   - Windows Command Prompt: `set GEMINI_API_KEY=YOUR_REAL_KEY`
   - macOS/Linux: `export GEMINI_API_KEY="YOUR_REAL_KEY"`
4. Open a terminal in this project folder.
5. Run: `mvn spring-boot:run`
6. Open: http://localhost:8080

If your API key is missing, the app will still start and upload documents, but AI questions will show a clear error until the key is configured.

## Test checklist
- Upload a valid text-based PDF.
- Upload a TXT file.
- Try a JPG or DOCX file and confirm the app rejects it.
- Try an empty file or a file over 5 MB.
- Ask a question that is answered by the document.
- Ask a question whose answer is not in the document; the prompt instructs the model not to invent information.
- Ask a question before uploading a document.
- Try without an API key and confirm the missing-key message is displayed.

## Simple architecture
Browser (HTML/CSS/Bootstrap/JavaScript)
  -> Spring Boot REST API
  -> DocumentService extracts text, splits it, and selects relevant chunks
  -> AiService sends the selected text and question to Gemini
  -> Answer returned to browser

## Important limitations
- This is a beginner-level demonstration, not a production document platform.
- Text is stored in server memory, not MySQL. Restarting the server clears the uploaded document.
- Keyword matching is a basic retrieval method, not semantic/vector search.
- PDF extraction works best with text-based PDFs. Scanned image PDFs need OCR, which is not included.
- One shared in-memory document is used by the demo server; multiple users should not use it concurrently.
- A real API key and network access are required for AI answers.
- Before public deployment, set GEMINI_API_KEY in the host's environment settings. Never commit secrets.
- Public deployment and live URL are not included automatically; deploy and test the project yourself.

## Deployment outline
1. Push the source to your GitHub repository.
2. Create a new Web Service on Render from the repository.
3. Build command: `mvn clean package`
4. Start command: `java -jar target/knowledge-assistant-0.0.1-SNAPSHOT.jar`
5. Add environment variable `GEMINI_API_KEY` in Render settings.
6. Deploy and open the public service URL. Because Spring Boot serves the frontend and API together, one service is sufficient for this beginner version.
7. Test uploads and AI answers using the public URL. Do not claim deployment is complete until you have tested it.

## Explain it in an interview
"I built a simple Knowledge Assistant using HTML, CSS, JavaScript, Bootstrap, Java, and Spring Boot. Users can upload PDF or TXT documents. The backend extracts the text and splits it into chunks. When a user asks a question, I use simple keyword matching to retrieve relevant chunks and send only those sections with the question to a Gemini model. The API returns an answer grounded in the document. I added basic validation and error handling. The current version keeps document text in memory and uses basic keyword retrieval, so it is a learning project rather than a production system."
