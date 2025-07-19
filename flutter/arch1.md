lib/
├── core/ # Shared, app-wide logic
│ ├── error/
│ │ └── exceptions.dart # Custom exceptions
│ │ └── failure.dart # Failure base class
│ ├── network/
│ │ └── api_client.dart # Dio/Retrofit setup
│ │ └── interceptors.dart # Token interceptor, logger
│ └── utils/
│ └── constants.dart # Static constants
│ └── validators.dart # Input validation helpers
│ └── formatters.dart # Date, number formatters
│
├── features/
│ ├── auth/
│ │ ├── data/
│ │ │ └── models/ # LoginRequest, UserModel
│ │ │ └── datasources/ # RemoteDataSource (API)
│ │ │ └── repositories/ # AuthRepositoryImpl
│ │ ├── domain/
│ │ │ └── entities/ # UserEntity, AuthToken
│ │ │ └── usecases/ # Login, Signup use cases
│ │ │ └── repositories/ # Abstract AuthRepository
│ │ └── presentation/
│ │ └── bloc/ # AuthBloc, states, events
│ │ └── pages/ # LoginPage, SignupPage
│ │ └── widgets/ # AuthForm, LoginButton
│
│ ├── feed/
│ │ ├── data/
│ │ │ └── models/ # FeedPostModel
│ │ │ └── datasources/ # FeedRemoteDataSource
│ │ │ └── repositories/ # FeedRepositoryImpl
│ │ ├── domain/
│ │ │ └── entities/ # PostEntity
│ │ │ └── usecases/ # GetFeedPosts
│ │ │ └── repositories/ # Abstract FeedRepository
│ │ └── presentation/
│ │ └── bloc/ # FeedBloc, states, events
│ │ └── pages/ # FeedPage
│ │ └── widgets/ # PostCard, StoryBar
│
│ ├── profile/
│ │ ├── data/
│ │ ├── domain/
│ │ └── presentation/
│ │ └── pages/ # ProfilePage
│ │ └── widgets/ # ProfileHeader, GridView
│
│ ├── post/
│ │ ├── data/
│ │ │ └── datasources/ # UploadPostDataSource
│ │ │ └── repositories/ # UploadPostRepositoryImpl
│ │ ├── domain/
│ │ │ └── usecases/ # UploadPost, DeletePost
│ │ └── presentation/
│ │ └── pages/ # CreatePostPage
│ │ └── widgets/ # ImagePicker, CaptionBox
│
│ └── chat/
│ ├── data/
│ │ └── models/ # MessageModel
│ │ └── datasources/ # ChatRemoteDataSource
│ ├── domain/
│ │ └── entities/ # MessageEntity
│ │ └── usecases/ # SendMessage, GetMessages
│ └── presentation/
│ └── bloc/ # ChatBloc
│ └── pages/ # ChatListPage, ChatRoomPage
│ └── widgets/ # ChatBubble, InputBar
│
├── services/ # Firebase, DB, Push, etc.
│ └── firebase_service.dart # Firestore, Storage
│ └── auth_service.dart # Firebase Auth helper
│ └── notification_service.dart# FCM setup
│ └── local_storage_service.dart# Hive or SharedPrefs
│
├── di/ # Dependency Injection
│ └── injector.dart # GetIt / Riverpod providers
│ └── modules/ # Feature-level bindings
│ └── auth_module.dart
│ └── feed_module.dart
│ └── post_module.dart
│
└── main.dart # App entry point
