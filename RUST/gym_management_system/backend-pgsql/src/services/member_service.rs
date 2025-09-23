use crate::db::DbPool;
use crate::models::member::{Member, NewMember};
use chrono::Utc;

pub async fn list_members(pool: &DbPool) -> Vec<Member> {
    sqlx::query_as::<_, Member>("SELECT * FROM Members ORDER BY join_date DESC")
        .fetch_all(pool)
        .await
        .unwrap()
}

pub async fn create_member(pool: &DbPool, data: NewMember) -> Member {
    sqlx::query_as::<_, Member>(
        "INSERT INTO Members 
        (first_name,last_name,gender,dob,phone,email,address,join_date,membership_status) 
        VALUES ($1,$2,$3,$4,$5,$6,$7,$8,'Active') RETURNING *"
    )
    .bind(data.first_name)
    .bind(data.last_name)
    .bind(data.gender)
    .bind(data.dob)
    .bind(data.phone)
    .bind(data.email)
    .bind(data.address)
    .bind(Utc::now().date_naive())
    .fetch_one(pool)
    .await
    .unwrap()
}
