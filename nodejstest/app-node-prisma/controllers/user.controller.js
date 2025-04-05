const { PrismaClient } = require("@prisma/client");
const prisma = new PrismaClient();

exports.createUser = async (req, res) => {
    try {
        const user = await prisma.user.create({ data: req.body });
        res.json(user);
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
};

exports.getAllUsers = async (req, res) => {
    const users = await prisma.user.findMany();
    res.json(users);
};

exports.getUserById = async (req, res) => {
    const user = await prisma.user.findUnique({
        where: { id: Number(req.params.id) },
    });
    if (user) res.json(user);
    else res.status(404).json({ error: "User not found" });
};

exports.updateUser = async (req, res) => {
    const user = await prisma.user.update({
        where: { id: Number(req.params.id) },
        data: req.body,
    });
    res.json(user);
};

exports.deleteUser = async (req, res) => {
    await prisma.user.delete({ where: { id: Number(req.params.id) } });
    res.json({ message: "User deleted" });
};

exports.upsertUser = async (req, res) => {
    const { email, name, password } = req.body;
    const user = await prisma.user.upsert({
        where: { email },
        update: { name, password },
        create: { email, name, password },
    });
    res.json(user);
};
