use crate::db::DbPool;
use crate::models::subscription::{MemberSubscription, NewSubscription};

pub async fn list_subscriptions(pool: &DbPool) -> Vec<MemberSubscription> {
    sqlx::query_as::<_, MemberSubscription>("SELECT * FROM MemberSubscriptions ORDER BY subscription_id ASC")
        .fetch_all(pool)
        .await
        .unwrap()
}

pub async fn create_subscription(pool: &DbPool, data: NewSubscription) -> MemberSubscription {
    sqlx::query_as::<_, MemberSubscription>(
        "INSERT INTO MemberSubscriptions (member_id, plan_id, start_date, end_date, status)
         VALUES ($1,$2,$3,$4,'Active') RETURNING *"
    )
    .bind(data.member_id)
    .bind(data.plan_id)
    .bind(data.start_date)
    .bind(data.end_date)
    .fetch_one(pool)
    .await
    .unwrap()
}
