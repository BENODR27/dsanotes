use serde::{Serialize, Deserialize};
use sqlx::FromRow;
use chrono::NaiveDate;

#[derive(Debug, Serialize, Deserialize, FromRow)]
pub struct MemberSubscription {
    pub subscription_id: i32,
    pub member_id: i32,
    pub plan_id: i32,
    pub start_date: NaiveDate,
    pub end_date: NaiveDate,
    pub status: String,
}

#[derive(Debug, Deserialize)]
pub struct NewSubscription {
    pub member_id: i32,
    pub plan_id: i32,
    pub start_date: NaiveDate,
    pub end_date: NaiveDate,
}
