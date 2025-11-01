# MelodyMind Project Structure

## 📁 Package Organization

```
com.sourav.melodymind/
├── 📦 config/                    # Configuration classes
│   ├── CacheConfig.java         # Cache configuration (Caffeine)
│   ├── JacksonConfig.java       # JSON serialization config
│   ├── MelodyMindProperties.java # Application properties
│   └── WebClientConfig.java     # HTTP client configuration
│
├── 📦 constants/                 # Application constants
│   ├── ApiConstants.java        # API endpoints and paths
│   └── ApplicationConstants.java # String literals and defaults
│
├── 📦 controller/                # REST API controllers
│   ├── FavoriteController.java  # User favorites management
│   ├── HealthController.java    # Health check endpoint
│   ├── MusicController.java     # Music recommendations API
│   ├── ProviderController.java  # Provider status monitoring
│   ├── SchedulerController.java # Scheduler management
│   ├── TestController.java      # API testing endpoints
│   └── WebController.java       # Web page routing
│
├── 📦 dto/                       # Data Transfer Objects
│   ├── ApiResponse.java         # Standard API response wrapper
│   ├── FavoriteRequestDto.java  # Favorite operation requests
│   ├── RecommendationRequestDto.java # Recommendation filters
│   └── SongDto.java             # Song data transfer object
│
├── 📦 entity/                    # JPA entities
│   ├── Song.java                # Song entity with metadata
│   └── UserFavorite.java        # User-song relationship
│
├── 📦 exception/                 # Custom exceptions
│   ├── GlobalExceptionHandler.java # Global error handling
│   ├── ResourceNotFoundException.java # 404 errors
│   └── ServiceException.java    # Service layer errors
│
├── 📦 provider/                  # Music provider plugin system
│   ├── 📦 config/
│   │   └── ProviderConfig.java  # Provider configurations
│   ├── 📦 impl/
│   │   ├── DeezerProviderPlugin.java # Deezer integration
│   │   ├── LastFmProviderPlugin.java # Last.fm integration
│   │   ├── LyricsOvhProvider.java    # Lyrics.ovh integration
│   │   └── SpotifyProviderPlugin.java # Spotify integration
│   ├── MusicProviderPlugin.java # Provider interface
│   ├── ProviderManager.java     # Provider lifecycle management
│   └── README.md               # Provider system documentation
│
├── 📦 repository/                # JPA repositories
│   ├── SongRepository.java      # Song data access
│   └── UserFavoriteRepository.java # Favorites data access
│
├── 📦 service/                   # Business logic interfaces
│   ├── 📦 impl/                 # Service implementations
│   │   ├── ChordsServiceImpl.java # Piano chords service
│   │   ├── DataInitializationServiceImpl.java # Sample data
│   │   ├── LyricsServiceImpl.java # Lyrics service
│   │   ├── MusicRecommendationServiceImpl.java # Main service
│   │   ├── SchedulerServiceImpl.java # Scheduled tasks
│   │   └── UserFavoriteServiceImpl.java # Favorites service
│   ├── ChordsService.java       # Piano chords interface
│   ├── DataInitializationService.java # Data initialization
│   ├── LyricsService.java       # Lyrics interface
│   ├── MusicRecommendationService.java # Main service interface
│   ├── SchedulerService.java    # Scheduler interface
│   ├── SchedulerStats.java      # Scheduler statistics
│   └── UserFavoriteService.java # Favorites interface
│
├── 📦 utils/                     # Utility classes
│   ├── CacheUtils.java          # Cache key generation
│   ├── DateTimeUtils.java       # Date/time operations
│   ├── JsonUtils.java           # JSON parsing utilities
│   ├── StringUtils.java         # String manipulation
│   └── ValidationUtils.java     # Data validation
│
└── MelodyMindApplication.java    # Main Spring Boot application
```

## 🏗️ Architecture Patterns

### 1. **Layered Architecture**
- **Controller Layer**: REST API endpoints
- **Service Layer**: Business logic and orchestration
- **Repository Layer**: Data access and persistence
- **Provider Layer**: External API integration

### 2. **Design Patterns Used**
- **Strategy Pattern**: Music provider plugins
- **Factory Pattern**: Provider manager
- **Repository Pattern**: JPA data access
- **DTO Pattern**: Data transfer objects
- **Builder Pattern**: Entity and DTO construction
- **Singleton Pattern**: Spring managed beans

### 3. **Plugin Architecture**
- Extensible provider system
- Interface-based design
- Priority-based execution
- Configuration-driven enablement

## 📋 Coding Standards

### 1. **Package Naming**
- Lowercase, descriptive names
- Logical grouping by functionality
- Clear separation of concerns

### 2. **Class Naming**
- PascalCase for classes
- Descriptive, intention-revealing names
- Consistent suffixes (Service, Controller, Repository, etc.)

### 3. **Constants Organization**
- Grouped by functionality
- Final classes with private constructors
- Static final fields with UPPER_SNAKE_CASE

### 4. **Utility Classes**
- Final classes, private constructors
- Static methods only
- Comprehensive JavaDoc documentation
- Null-safe implementations

## 🔧 Configuration Management

### 1. **Application Properties**
```yaml
melodymind:
  cache:
    refresh-interval: 300000
  api:
    timeout: 5000
    retry-attempts: 3
  providers:
    spotify:
      enabled: true
      client-id: ${SPOTIFY_CLIENT_ID}
```

### 2. **Environment Variables**
- `SPOTIFY_CLIENT_ID` - Spotify API credentials
- `LASTFM_API_KEY` - Last.fm API key
- `GENIUS_API_KEY` - Genius API key

## 🚀 Key Features

### 1. **Multi-Provider Aggregation**
- Concurrent API calls using CompletableFuture
- Automatic deduplication
- Fallback mechanisms

### 2. **Intelligent Caching**
- Different expiration times per cache type
- Scheduled refresh at midnight
- User-specific cache keys

### 3. **Real API Integration**
- Lyrics.ovh for song lyrics
- Mock providers with realistic behavior
- Comprehensive error handling

### 4. **Monitoring & Administration**
- Health check endpoints
- Provider status monitoring
- Scheduler statistics
- Admin dashboard

## 📊 Performance Optimizations

### 1. **Caching Strategy**
- Recommendations: 1-hour expiration
- Trending: 24-hour expiration
- Lyrics/Chords: 7-day expiration

### 2. **Database Optimization**
- Batch processing for cleanup
- Indexed queries
- Connection pooling

### 3. **Concurrent Processing**
- Virtual threads support
- Parallel provider calls
- Async service methods

## 🧪 Testing Strategy

### 1. **Test Endpoints**
- `/api/test/lyrics` - Test lyrics API
- `/api/test/chords` - Test chords API
- `/api/test/async-test` - Test concurrent processing
- `/api/test/thread-info` - Thread monitoring

### 2. **Test Pages**
- `http://localhost:8080/test.html` - API testing interface
- `http://localhost:8080/admin.html` - Admin dashboard

## 📝 Documentation Standards

### 1. **JavaDoc Requirements**
- All public classes and methods
- Parameter descriptions
- Return value descriptions
- Exception documentation

### 2. **Code Comments**
- Complex business logic
- Algorithm explanations
- Configuration rationale

### 3. **README Files**
- Package-level documentation
- Setup instructions
- Usage examples

## 🔒 Security Considerations

### 1. **Input Validation**
- Parameter validation in controllers
- DTO validation annotations
- Utility validation methods

### 2. **Error Handling**
- Global exception handler
- Sanitized error messages
- Proper HTTP status codes

### 3. **Configuration Security**
- Environment variables for secrets
- No hardcoded credentials
- Secure default values

This structure follows enterprise-level Java development standards and provides a solid foundation for scalable, maintainable code.