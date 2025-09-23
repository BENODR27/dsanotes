use crate::db::DbPool;
use crate::models::trainer::{Trainer, NewTrainer};

pub async fn list_trainers(pool: &DbPool) -> Vec<Trainer> {
    sqlx::query_as::<_, Trainer>("SELECT * FROM Trainers ORDER BY trainer_id ASC")
        .fetch_all(pool)
        .await
        .unwrap()
}

pub async fn create_trainer(pool: &DbPool, data: NewTrainer) -> Trainer {
    sqlx::query_as::<_, Trainer>(
        "INSERT INTO Trainers (first_name,last_name,specialization,phone,email)
         VALUES ($1,$2,$3,$4,$5) RETURNING *"
    )
    .bind(data.first_name)
    .bind(data.last_name)
    .bind(data.specialization)
    .bind(data.phone)
    .bind(data.email)
    .fetch_one(pool)
    .await
    .unwrap()
}
