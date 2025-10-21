Perfect ✅ — let’s go **fully end-to-end**, exactly how you’d configure **Amazon S3 Event Notifications** with all destinations (**Lambda**, **SQS**, **SNS**) — including **IAM permissions**, **SQS/SNS creation**, **policy setup**, **optional filters**, and a working **Node.js Lambda example**.

This will give you a **complete enterprise-grade configuration** that also aligns with **AWS exam** and **real-world deployment** standards.

---

# 🚀 **Full End-to-End Setup: S3 Event Notifications with Lambda, SQS, and SNS**

---

## 🧭 Overview Architecture

```
S3 Bucket (my-app-bucket)
   ├── Lambda Function (process-uploaded-images)
   ├── SQS Queue (upload-events-queue)
   └── SNS Topic (upload-alerts-topic)
```

S3 triggers notifications when an **object is uploaded, removed, or restored**, and routes events to **Lambda**, **SQS**, or **SNS**.

---

## 🪣 **Step 1: Create an S3 Bucket**

1. Go to **AWS Console → S3 → Create bucket**
2. Name your bucket: `my-app-bucket`
3. Choose region: `us-east-1`
4. Leave defaults (block public access ON)
5. Click **Create bucket**

---

## 🧰 **Step 2: Create an IAM Role for Lambda**

1. Go to **IAM → Roles → Create Role**
2. Select **AWS Service → Lambda**
3. Attach the following policies:

   - **AWSLambdaBasicExecutionRole**
   - **AmazonS3ReadOnlyAccess**

4. Name the role: `LambdaS3EventRole`

✅ **Custom Inline Policy (optional for fine-grained S3 access):**

```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": ["s3:GetObject"],
      "Resource": "arn:aws:s3:::my-app-bucket/*"
    }
  ]
}
```

---

## 🧠 **Step 3: Create the Lambda Function**

1. Go to **Lambda → Create Function**
2. Choose:

   - **Author from scratch**
   - Name: `process-uploaded-images`
   - Runtime: `Node.js 18.x`
   - Role: **Use existing role** → `LambdaS3EventRole`

3. Create function.

### ✨ Lambda Code Example

```javascript
exports.handler = async (event) => {
  console.log("S3 Event:", JSON.stringify(event, null, 2));

  for (const record of event.Records) {
    const bucket = record.s3.bucket.name;
    const key = decodeURIComponent(record.s3.object.key.replace(/\+/g, " "));
    const eventType = record.eventName;
    console.log(`Bucket: ${bucket}, Key: ${key}, Event: ${eventType}`);

    if (key.endsWith(".jpg") || key.endsWith(".png")) {
      console.log(`Processing image file: ${key}`);
      // Your custom image processing logic here
    }
  }

  return { statusCode: 200, body: "Event processed successfully" };
};
```

Deploy this function.

---

## 📬 **Step 4: Create an SQS Queue**

1. Go to **SQS → Create Queue**
2. Type: **Standard**
3. Name: `upload-events-queue`
4. Keep default settings, create queue.

### 🔐 Add Permissions to Allow S3 to Send Messages

Go to **SQS → Queue → Access Policy → Edit** and replace with:

```json
{
  "Version": "2012-10-17",
  "Id": "SQSPolicy",
  "Statement": [
    {
      "Effect": "Allow",
      "Principal": { "Service": "s3.amazonaws.com" },
      "Action": "SQS:SendMessage",
      "Resource": "arn:aws:sqs:us-east-1:YOUR_ACCOUNT_ID:upload-events-queue",
      "Condition": {
        "ArnLike": {
          "aws:SourceArn": "arn:aws:s3:::my-app-bucket"
        }
      }
    }
  ]
}
```

✅ Replace `YOUR_ACCOUNT_ID` with your AWS account ID.
✅ This policy allows S3 to send event notifications to this queue.

---

## 📣 **Step 5: Create an SNS Topic**

1. Go to **SNS → Create Topic**
2. Type: **Standard**
3. Name: `upload-alerts-topic`
4. Create topic.

### 📨 Add an Email Subscription

1. In the topic, click **Create subscription**
2. Protocol: **Email**
3. Endpoint: enter your email address.
4. Confirm the subscription via email you’ll receive from AWS.

### 🔐 Add Permissions for S3 to Publish

Go to **SNS → Topic → Access Policy → Edit** and use:

```json
{
  "Version": "2012-10-17",
  "Id": "SNSTopicPolicy",
  "Statement": [
    {
      "Effect": "Allow",
      "Principal": { "Service": "s3.amazonaws.com" },
      "Action": "SNS:Publish",
      "Resource": "arn:aws:sns:us-east-1:YOUR_ACCOUNT_ID:upload-alerts-topic",
      "Condition": {
        "ArnLike": {
          "aws:SourceArn": "arn:aws:s3:::my-app-bucket"
        }
      }
    }
  ]
}
```

✅ This allows the S3 bucket to publish notifications to the SNS topic.

---

## ⚙️ **Step 6: Configure S3 Event Notifications**

Now we’ll configure all three destinations (Lambda, SQS, SNS).

1. Go to **S3 → your bucket → Properties → Event Notifications**
2. Click **Create event notification**
3. Enter a name, e.g., `FileUploadEvent`
4. **Event types**: choose `All object create events`
5. **Prefix (optional):** `images/`
6. **Suffix (optional):** `.jpg`
7. **Destination:**

   - Choose one destination at a time (repeat for each if needed)

     - **Lambda function** → select `process-uploaded-images`
     - **SQS queue** → select `upload-events-queue`
     - **SNS topic** → select `upload-alerts-topic`

8. Save each notification configuration.

---

## 🧪 **Step 7: Testing the Setup**

### ✅ Lambda

1. Upload a file (e.g., `images/test.jpg`) to S3.
2. Go to **CloudWatch Logs → process-uploaded-images**.
3. You should see a log entry with the event details.

### ✅ SQS

1. Go to **SQS → upload-events-queue → Messages → Poll messages**.
2. You should see the S3 event message.

### ✅ SNS

1. Check your email inbox — you should receive a notification that a file was uploaded.

---

## ✅ **Step 8: Optional Filters (Prefix & Suffix)**

In the S3 event configuration:

| Filter     | Example Value | Description                                          |
| ---------- | ------------- | ---------------------------------------------------- |
| **Prefix** | `images/`     | Only trigger for uploads inside the `images/` folder |
| **Suffix** | `.jpg`        | Only trigger for `.jpg` files                        |

This ensures that your Lambda/SQS/SNS only receives relevant notifications — reducing unnecessary invocations and costs.

---

## ✅ Key Takeaways

| Component                 | Purpose                                        |
| ------------------------- | ---------------------------------------------- |
| **S3**                    | Source of event (upload/delete/restore)        |
| **Lambda**                | Real-time serverless processing                |
| **SQS**                   | Durable event queue for asynchronous workflows |
| **SNS**                   | Broadcast alerts to multiple subscribers       |
| **IAM Policies**          | Control who can send/receive event data        |
| **Prefix/Suffix Filters** | Optimize triggers and cost efficiency          |

---

Would you like me to **generate a clean architecture diagram** (S3 → Lambda, SQS, SNS + IAM + filters) to visually show the entire setup flow for documentation or presentations?
