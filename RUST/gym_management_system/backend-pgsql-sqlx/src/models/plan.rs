use serde::{Serialize, Deserialize};
use sqlx::FromRow;

#[derive(Debug, Serialize, Deserialize, FromRow)]
pub struct MembershipPlan {
    pub plan_id: i32,
    pub plan_name: String,
    pub duration_months: i32,
    pub fee: f64,
    pub description: Option<String>,
}

#[derive(Debug, Deserialize)]
pub struct NewPlan {
    pub plan_name: String,
    pub duration_months: i32,
    pub fee: f64,
    pub description: Option<String>,
}
