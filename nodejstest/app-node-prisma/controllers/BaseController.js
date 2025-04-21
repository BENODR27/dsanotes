// utils/BaseController.js
class BaseController {
    constructor(model, include = {}) {
        this.model = model;
        this.include = include;
    }

    async create(req, res) {
        try {
            const data = req.body;
            const created = await this.model.create({ data, include: this.include });
            res.status(201).json(created);
        } catch (err) {
            res.status(500).json({ error: err.message });
        }
    };

    findAll = async (req, res) => {
        try {
            const all = await this.model.findMany({ include: this.include });
            res.json(all);
        } catch (err) {
            res.status(500).json({ error: err.message });
        }
    };

    findById = async (req, res) => {
        try {
            const { id } = req.params;
            const item = await this.model.findUnique({
                where: { id: Number(id) },
                include: this.include,
            });
            if (item) res.json(item);
            else res.status(404).json({ message: "Not found" });
        } catch (err) {
            res.status(500).json({ error: err.message });
        }
    };

    update = async (req, res) => {
        try {
            const { id } = req.params;
            const data = req.body;
            const updated = await this.model.update({
                where: { id: Number(id) },
                data,
            });
            res.json(updated);
        } catch (err) {
            res.status(500).json({ error: err.message });
        }
    };

    delete = async (req, res) => {
        try {
            const { id } = req.params;
            await this.model.delete({ where: { id: Number(id) } });
            res.json({ message: "Deleted" });
        } catch (err) {
            res.status(500).json({ error: err.message });
        }
    };
}

module.exports = BaseController;
