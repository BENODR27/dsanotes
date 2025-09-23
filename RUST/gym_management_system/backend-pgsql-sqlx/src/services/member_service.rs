use crate::db::DbPool;
use crate::models::member::{Member, NewMember};
use chrono::Utc;
use sqlx::Error;

/// Fetch all members
pub async fn list_members(pool: &DbPool) -> Vec<Member> {
    sqlx::query_as::<_, Member>("SELECT * FROM Members ORDER BY join_date DESC")
        .fetch_all(pool)
        .await
        .unwrap()
}

/// Fetch a single member by ID
pub async fn get_member_by_id(pool: &DbPool, member_id: i32) -> Option<Member> {
    sqlx::query_as::<_, Member>("SELECT * FROM Members WHERE member_id = $1")
        .bind(member_id)
        .fetch_optional(pool)
        .await
        .unwrap()
}

/// Create a new member
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

/// Update a member's details
pub async fn update_member(pool: &DbPool, member_id: i32, data: NewMember) -> Option<Member> {
    sqlx::query_as::<_, Member>(
        "UPDATE Members 
        SET first_name=$1,last_name=$2,gender=$3,dob=$4,phone=$5,email=$6,address=$7
        WHERE member_id=$8 RETURNING *"
    )
    .bind(data.first_name)
    .bind(data.last_name)
    .bind(data.gender)
    .bind(data.dob)
    .bind(data.phone)
    .bind(data.email)
    .bind(data.address)
    .bind(member_id)
    .fetch_optional(pool)
    .await
    .unwrap()
}

/// Delete a member
pub async fn delete_member(pool: &DbPool, member_id: i32) -> Result<u64, Error> {
    let result = sqlx::query("DELETE FROM Members WHERE member_id=$1")
        .bind(member_id)
        .execute(pool)
        .await?;
    Ok(result.rows_affected())
}

/// Search members by name
pub async fn search_members(pool: &DbPool, query: &str) -> Vec<Member> {
    let like_query = format!("%{}%", query);
    sqlx::query_as::<_, Member>(
        "SELECT * FROM Members WHERE first_name LIKE $1 OR last_name LIKE $1"
    )
    .bind(like_query)
    .fetch_all(pool)
    .await
    .unwrap()
}

/// Activate or deactivate a member
pub async fn set_membership_status(pool: &DbPool, member_id: i32, status: &str) -> Option<Member> {
    sqlx::query_as::<_, Member>(
        "UPDATE Members SET membership_status=$1 WHERE member_id=$2 RETURNING *"
    )
    .bind(status)
    .bind(member_id)
    .fetch_optional(pool)
    .await
    .unwrap()
}
