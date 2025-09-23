use serde::{Serialize, Deserialize};
use sqlx::FromRow;
use chrono::NaiveDate;

#[derive(Debug, Serialize, Deserialize, FromRow)]
pub struct Member {
    pub member_id: i32,
    pub first_name: String,
    pub last_name: String,
    pub gender: Option<String>,
    pub dob: Option<NaiveDate>,
    pub phone: Option<String>,
    pub email: Option<String>,
    pub address: Option<String>,
    pub join_date: NaiveDate,
    pub membership_status: String,
}

#[derive(Debug, Deserialize)]
pub struct NewMember {
    pub first_name: String,
    pub last_name: String,
    pub gender: Option<String>,
    pub dob: Option<NaiveDate>,
    pub phone: Option<String>,
    pub email: Option<String>,
    pub address: Option<String>,
}
