const { PrismaClient } = require("@prisma/client");
const prisma = new PrismaClient();

// Create order with items
exports.createOrder = async (req, res) => {
    const { userId, items } = req.body;

    try {
        const order = await prisma.order.create({
            data: {
                userId,
                orderItems: {
                    create: items.map(item => ({
                        productId: item.productId,
                        quantity: item.quantity,
                    })),
                },
            },
            include: {
                orderItems: true,
            },
        });
        res.json(order);
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
};

exports.getAllOrders = async (req, res) => {
    const orders = await prisma.order.findMany({
        include: {
            orderItems: {
                include: {
                    product: true,
                },
            },
            user: true,
        },
    });
    res.json(orders);
};

exports.getOrderById = async (req, res) => {
    const { id } = req.params;
    const order = await prisma.order.findUnique({
        where: { id: Number(id) },
        include: {
            orderItems: {
                include: { product: true },
            },
            user: true,
        },
    });
    if (order) res.json(order);
    else res.status(404).json({ message: "Order not found" });
};

exports.updateOrderStatus = async (req, res) => {
    const { id } = req.params;
    const { status } = req.body;

    const order = await prisma.order.update({
        where: { id: Number(id) },
        data: { status },
    });

    res.json(order);
};

exports.deleteOrder = async (req, res) => {
    const { id } = req.params;
    await prisma.order.delete({ where: { id: Number(id) } });
    res.json({ message: "Order deleted" });
};
