curl -fsSL https://ollama.com/install.sh | sh

https://ollama.com/library -- for models

ollama pull qwen2:0.5b

ollama serve

app.post('/api/pms/chat', async (req, res) => {
try {
const prompt = req.body.prompt || "Hello!";
const resp = await axios.post('http://localhost:11434/api/generate', {
model: "qwen2:0.5b",
prompt: prompt,
stream: false
});
res.json(resp.data);
} catch (err) {
console.error(err.response?.data || err.message);
res.status(500).json({ error: "Error talking to model" });
}
});




https://nvidia-nemo.github.io/Skills/basics/inference/#with-code-execution