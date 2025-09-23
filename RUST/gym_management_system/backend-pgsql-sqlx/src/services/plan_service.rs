use crate::db::DbPool;
use crate::models::plan::{MembershipPlan, NewPlan};

pub async fn list_plans(pool: &DbPool) -> Vec<MembershipPlan> {
    sqlx::query_as::<_, MembershipPlan>("SELECT * FROM MembershipPlans ORDER BY plan_id ASC")
        .fetch_all(pool)
        .await
        .unwrap()
}

pub async fn create_plan(pool: &DbPool, data: NewPlan) -> MembershipPlan {
    sqlx::query_as::<_, MembershipPlan>(
        "INSERT INTO MembershipPlans (plan_name,duration_months,fee,description) 
         VALUES ($1,$2,$3,$4) RETURNING *"
    )
    .bind(data.plan_name)
    .bind(data.duration_months)
    .bind(data.fee)
    .bind(data.description)
    .fetch_one(pool)
    .await
    .unwrap()
}
