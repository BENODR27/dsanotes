use axum::{Json, Router, routing::{get, post}, extract::State};
use std::sync::Arc;
use crate::{db::DbPool, services::trainer_service, models::trainer::{Trainer, NewTrainer}};

#[derive(Clone)]
pub struct TrainerState {
    pub pool: DbPool,
}

pub fn trainer_routes(pool: DbPool) -> Router {
    Router::new()
        .route("/trainers", get(list_trainers).post(create_trainer))
        .with_state(Arc::new(TrainerState { pool }))
}

async fn list_trainers(State(state): State<Arc<TrainerState>>) -> Json<Vec<Trainer>> {
    let trainers = trainer_service::list_trainers(&state.pool).await;
    Json(trainers)
}

async fn create_trainer(State(state): State<Arc<TrainerState>>, Json(payload): Json<NewTrainer>) -> Json<Trainer> {
    let trainer = trainer_service::create_trainer(&state.pool, payload).await;
    Json(trainer)
}
