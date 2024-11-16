import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

// Data Class cho Post
data class Post(val id: Int, val userId: Int, val title: String, val body: String)

// API Service
interface ApiService {
    @GET("posts")
    suspend fun getPosts(): List<Post>

    @POST("posts")
    suspend fun createPost(@Body post: Post): Response<Post>
}

// ViewModel
class PostViewModel : ViewModel() {
    private val api = Retrofit.Builder()
        .baseUrl("https://jsonplaceholder.typicode.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)

    var posts by mutableStateOf<List<Post>>(emptyList())
        private set

    init {
        fetchPosts()
    }

    // Hàm lấy dữ liệu (GET)
    private fun fetchPosts() {
        viewModelScope.launch {
            posts = api.getPosts()
        }
    }

    // Hàm tạo mới dữ liệu (POST)
    fun createNewPost(post: Post) {
        viewModelScope.launch {
            val response = api.createPost(post)
            if (response.isSuccessful) {
                response.body()?.let {
                    posts = posts + it
                }
            }
        }
    }
}

// UI Composable để hiển thị danh sách Post
@Composable
fun PostList(viewModel: PostViewModel = PostViewModel()) {
    Column {
        CreatePostButton(viewModel)
        LazyColumn {
            items(viewModel.posts) { post ->
                Text(text = post.title, style = MaterialTheme.typography.titleMedium)
                Text(text = post.body, style = MaterialTheme.typography.bodyMedium)
                Divider()
            }
        }
    }
}

// Nút tạo Post mới
@Composable
fun CreatePostButton(viewModel: PostViewModel) {
    Button(onClick = {
        val newPost = Post(id = 0, userId = 1, title = "New Post", body = "This is a new post body.")
        viewModel.createNewPost(newPost)
    }) {
        Text("Create Post")
    }
}

@Preview
@Composable
fun PostListPreview() {
    PostList()
}
