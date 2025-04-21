import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

class MemberService {
    private val scope = CoroutineScope(Dispatchers.Default)
    private val _members = MutableStateFlow<List<Member>>(emptyList())
    val members: StateFlow<List<Member>> = _members

    private val memberChannel = Channel<Member>()

    init {
        scope.launch {
            memberChannel.consumeAsFlow()
                .collect { member ->
                    _members.value = (_members.value + member)
                        .sortedByDescending { it.repoCount }
                }
        }
    }

    suspend fun addMember(username: String, repoCount: Int) {
        memberChannel.send(Member(username, repoCount))
    }

    fun clearMembers() {
        _members.value = emptyList()
    }

    fun cancel() {
        scope.cancel()
    }
}