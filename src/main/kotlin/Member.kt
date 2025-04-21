data class Member(val username: String, val repoCount: Int) {
    override fun toString(): String = "$username - $repoCount репозиториев"
}