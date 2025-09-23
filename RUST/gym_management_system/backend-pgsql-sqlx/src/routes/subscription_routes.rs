use axum::{Json, Router, routing::{get, post}, extract::State};
use std::sync::Arc;
use crate::{db::DbPool, services::subscription_service, models::subscription::{MemberSubscription, NewSubscription}};

#[derive(Clone)]
pub struct SubscriptionState {
    pub pool: DbPool,
}

pub fn subscription_routes(pool: DbPool) -> Router {
    Router::new()
        .route("/subscriptions", get(list_subscriptions).post(create_subscription))
        .with_state(Arc::new(SubscriptionState { pool }))
}

async fn list_subscriptions(State(state): State<Arc<SubscriptionState>>) -> Json<Vec<MemberSubscription>> {
    let subscriptions = subscription_service::list_subscriptions(&state.pool).await;
    Json(subscriptions)
}

async fn create
