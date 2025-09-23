mod config;
mod db;
mod models;
mod services;
mod routes;

use axum::Router;
use dotenvy::dotenv;
use std::net::SocketAddr;
use routes::{member_routes::member_routes, plan_routes::plan_routes, subscription_routes::subscription_routes, trainer_routes::trainer_routes};
use db::init_db;

#[tokio::main]
async fn main() {
    dotenv().ok();
    let db_url = std::env::var("DATABASE_URL").expect("DATABASE_URL not set");
    let pool = init_db(&db_url).await;

    let app = Router::new()
        .merge(member_routes(pool.clone()))
        .merge(plan_routes(pool.clone()))
        .merge(subscription_routes(pool.clone()))
        .merge(trainer_routes(pool.clone()));

    let addr = SocketAddr::from(([0,0,0,0], 3000));
    println!("Backend running at {}", addr);
    axum::Server::bind(&addr).serve(app.into_make_service()).await.unwrap();
}
