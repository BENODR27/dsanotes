use serde::{Serialize, Deserialize};
use sqlx::FromRow;

#[derive(Debug, Serialize, Deserialize, FromRow)]
pub struct Trainer {
    pub trainer_id: i32,
    pub first_name: String,
    pub last_name: String,
    pub specialization: Option<String>,
    pub phone: Option<String>,
    pub email: Option<String>,
}

#[derive(Debug, Deserialize)]
pub struct NewTrainer {
    pub first_name: String,
    pub last_name: String,
    pub specialization: Option<String>,
    pub phone: Option<String>,
    pub email: Option<String>,
}
