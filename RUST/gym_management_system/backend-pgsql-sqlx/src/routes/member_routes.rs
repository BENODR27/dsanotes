use axum::{Json, Router, routing::{get, post}, extract::State};
use std::sync::Arc;
use crate::{db::DbPool, services::member_service, models::member::{NewMember, Member}};

#[derive(Clone)]
pub struct MemberState {
    pub pool: DbPool,
}

pub fn member_routes(pool: DbPool) -> Router {
    Router::new()
        .route("/members", get(list_members).post(create_member))
        .with_state(Arc::new(MemberState { pool }))
}

async fn list_members(State(state): State<Arc<MemberState>>) -> Json<Vec<Member>> {
    Json(member_service::list_members(&state.pool).await)
}

async fn create_member(State(state): State<Arc<MemberState>>, Json(payload): Json<NewMember>) -> Json<Member> {
    Json(member_service::create_member(&state.pool, payload).await)
}
