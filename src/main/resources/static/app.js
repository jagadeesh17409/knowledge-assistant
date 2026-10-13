const uploadForm = document.getElementById("uploadForm");
const questionForm = document.getElementById("questionForm");
const uploadStatus = document.getElementById("uploadStatus");
const answerBox = document.getElementById("answerBox");
const uploadButton = document.getElementById("uploadButton");
const askButton = document.getElementById("askButton");

function showMessage(element, message, isError = false) {
  element.textContent = message;
  element.className = isError ? "mt-3 status-error" : "mt-3 status-success";
}

uploadForm.addEventListener("submit", async (event) => {
  event.preventDefault();
  const file = document.getElementById("documentFile").files[0];
  if (!file) return showMessage(uploadStatus, "Please choose a file.", true);
  const extension = file.name.toLowerCase().split(".").pop();
  if (!["pdf", "txt"].includes(extension)) return showMessage(uploadStatus, "Only PDF and txt files are supported.", true);
  if (file.size > 5 * 1024 * 1024) return showMessage(uploadStatus, "File must be smaller than 5 MB.", true);

  const formData = new FormData();
  formData.append("file", file);
  uploadButton.disabled = true;
  showMessage(uploadStatus, "Uploading and reading document...");
  try {
    const response = await fetch("/api/upload", { method: "POST", body: formData });
    const data = await response.json();
    if (!response.ok) throw new Error(data.error || "Upload failed.");
    showMessage(uploadStatus, data.message);
    answerBox.textContent = "Document ready. Enter a question above.";
  } catch (error) {
    showMessage(uploadStatus, error.message || "Could not connect to the server.", true);
  } finally {
    uploadButton.disabled = false;
  }
});

questionForm.addEventListener("submit", async (event) => {
  event.preventDefault();
  const question = document.getElementById("question").value.trim();
  if (!question) { answerBox.textContent = "Please enter a question."; return; }
  askButton.disabled = true;
  answerBox.textContent = "Thinking... Please wait.";
  try {
    const response = await fetch("/api/ask", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ question })
    });
    const data = await response.json();
    if (!response.ok) throw new Error(data.error || "Question failed.");
    answerBox.textContent = data.answer;
  } catch (error) {
    answerBox.textContent = "Error: " + (error.message || "Could not connect to the server.");
  } finally {
    askButton.disabled = false;
  }
});