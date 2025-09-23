use sqlx::{Pool, Postgres};

pub type DbPool = Pool<Postgres>;

pub async fn init_db(db_url: &str) -> DbPool {
    sqlx::PgPool::connect(db_url)
        .await
        .expect("Failed to connect to DB")
}
