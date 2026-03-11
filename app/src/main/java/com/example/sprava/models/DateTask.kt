import java.util.Date

data class DateTask(
    val id: Int,
    val text: String,
    val description: String,
    val startDate: Date?,
    val endDate: Date?
)