use axum::{Json, Router, routing::{get, post}, extract::State};
use std::sync::Arc;
use crate::{db::DbPool, services::plan_service, models::plan::{MembershipPlan, NewPlan}};

#[derive(Clone)]
pub struct PlanState {
    pub pool: DbPool,
}

pub fn plan_routes(pool: DbPool) -> Router {
    Router::new()
        .route("/plans", get(list_plans).post(create_plan))
        .with_state(Arc::new(PlanState { pool }))
}

async fn list_plans(State(state): State<Arc<PlanState>>) -> Json<Vec<MembershipPlan>> {
    let plans = plan_service::list_plans(&state.pool).await;
    Json(pl
