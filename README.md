# Imageboard Clean Architecture Client

*This is the part of project which includes back-end and mobile client.* 

**Also check:**
[Golang Clean Architecture Back-End](https://github.com/numq/golang-clean-architecture-imageboard-backend/)

## Project description:
  **Simple anonymous imageboard which allows to create threads, posts and reply to users by quoting their posts.**
  
## Techs:
*Platform:*
- **Android**
- **Architecture Components (ViewModel, Lifecycle)**
- **Jetpack Compose**
- **Navigation Component**
- **Accompanist Permissions**
- **Coil**

*Common:*
- **Kotlin**
- **Coroutines/Flow**
- **Arrow-KT**
- **Hilt**

*Data:*
- **Protobuf**
- **gRPC**
- **Room**

## Structure:
- `Data`\
*Local and remote data sources.*
- `Domain`\
*Abstract entities & repositories.*
- `Platform`\
*Platform dependent components: DI, extension functions, navigation, handlers, etc.*
- `Presentation`\
*UI screens.*
- `UseCase`\
*Most likely: use case for each repository method.*

### Thoughts:
*Soon*
