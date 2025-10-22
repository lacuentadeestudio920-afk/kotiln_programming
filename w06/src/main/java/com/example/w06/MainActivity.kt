package com.example.w06

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.random.Random

// 메인 액티비티
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BubbleGameScreen()
                }
            }
        }
    }
}

// 버블 타입 enum
enum class BubbleType {
    NORMAL,   // 일반 버블
    BOMB,     // 폭탄 버블 (점수 -10)
    BONUS     // 보너스 버블 (점수 +5)
}

// 🫧 버블 데이터 클래스
data class Bubble(
    val id: Int,
    val position: Offset,
    val radius: Float,
    val color: Color,
    val type: BubbleType = BubbleType.NORMAL,
    val creationTime: Long = System.currentTimeMillis(),
    val velocityX: Float = Random.nextFloat() * 4 - 2,
    val velocityY: Float = Random.nextFloat() * 4 - 2
)

// 게임 전체 화면
@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun BubbleGameScreen() {
    var bubbles by remember { mutableStateOf<List<Bubble>>(emptyList()) }
    var score by remember { mutableStateOf(0) }
    var isGameOver by remember { mutableStateOf(false) }
    var timeLeft by remember { mutableStateOf(60) }

    // 타이머 로직
    LaunchedEffect(isGameOver) {
        if (!isGameOver && timeLeft > 0) {
            while (true) {
                delay(1000L)
                timeLeft--
                if (timeLeft == 0) {
                    isGameOver = true
                    break
                }
                val currentTime = System.currentTimeMillis()
                bubbles = bubbles.filter {
                    currentTime - it.creationTime < 3000
                }
            }
        }
    }

    // 전체 레이아웃
    Column(modifier = Modifier.fillMaxSize()) {
        GameStatusRow(score = score, timeLeft = timeLeft)

        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val density = LocalDensity.current
            val canvasWidthPx = with(density) { maxWidth.toPx() }
            val canvasHeightPx = with(density) { maxHeight.toPx() }
            val maxWidthDp = maxWidth
            val maxHeightDp = maxHeight

            // 🎬 버블 이동 및 생성 로직
            LaunchedEffect(key1 = isGameOver) {
                if (!isGameOver) {
                    while (true) {
                        delay(16)

                        // 버블이 없을 경우 3개 생성
                        if (bubbles.isEmpty()) {
                            val newBubbles = List(3) {
                                createRandomBubble(maxWidthDp.value, maxHeightDp.value)
                            }
                            bubbles = newBubbles
                        }

                        // 랜덤 새 버블 생성
                        if (Random.nextFloat() < 0.05f && bubbles.size < 15) {
                            val newBubble = createRandomBubble(maxWidthDp.value, maxHeightDp.value)
                            bubbles = bubbles + newBubble
                        }

                        // 버블 이동 처리 (화면 경계 반사 포함)
                        bubbles = bubbles.map { bubble ->
                            var newX = bubble.position.x + bubble.velocityX
                            var newY = bubble.position.y + bubble.velocityY
                            var newVx = bubble.velocityX
                            var newVy = bubble.velocityY

                            // 경계 충돌 체크
                            if (newX - bubble.radius < 0 || newX + bubble.radius > maxWidthDp.value) {
                                newVx *= -1
                                newX = newX.coerceIn(bubble.radius, maxWidthDp.value - bubble.radius)
                            }
                            if (newY - bubble.radius < 0 || newY + bubble.radius > maxHeightDp.value) {
                                newVy *= -1
                                newY = newY.coerceIn(bubble.radius, maxHeightDp.value - bubble.radius)
                            }

                            bubble.copy(
                                position = Offset(x = newX, y = newY),
                                velocityX = newVx,
                                velocityY = newVy
                            )
                        }
                    }
                }
            }

            // 🫧 버블 렌더링
            Box(modifier = Modifier.fillMaxSize()) {
                bubbles.forEach { bubble ->
                    BubbleComposable(bubble = bubble) {
                        // 버블 타입에 따라 점수 변경
                        when (bubble.type) {
                            BubbleType.NORMAL -> score += 1
                            BubbleType.BOMB -> score = maxOf(0, score - 10) // 0 이하로 떨어지지 않게
                            BubbleType.BONUS -> score += 5
                        }
                        bubbles = bubbles.filterNot { it.id == bubble.id }
                    }
                }
            }
        }
    }
}

// 랜덤 버블 생성 함수
fun createRandomBubble(maxWidth: Float, maxHeight: Float): Bubble {
    val randomValue = Random.nextFloat()

    // 70% 일반, 15% 폭탄, 15% 보너스
    val bubbleType = when {
        randomValue < 0.70f -> BubbleType.NORMAL
        randomValue < 0.85f -> BubbleType.BOMB
        else -> BubbleType.BONUS
    }

    // 버블 타입에 따른 색상 설정
    val color = when (bubbleType) {
        BubbleType.NORMAL -> Color(
            Random.nextInt(256),
            Random.nextInt(256),
            Random.nextInt(256),
            200
        )
        BubbleType.BOMB -> Color(255, 50, 50, 220) // 빨간색
        BubbleType.BONUS -> Color(255, 215, 0, 220) // 금색
    }

    return Bubble(
        id = Random.nextInt(),
        position = Offset(
            x = Random.nextFloat() * maxWidth,
            y = Random.nextFloat() * maxHeight
        ),
        radius = Random.nextFloat() * 25 + 25,
        color = color,
        type = bubbleType
    )
}

// 버블 그리기
@Composable
fun BubbleComposable(bubble: Bubble, onClick: () -> Unit) {
    Canvas(
        modifier = Modifier
            .offset(
                x = (bubble.position.x - bubble.radius).dp,
                y = (bubble.position.y - bubble.radius).dp
            )
            .size((bubble.radius * 2).dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
    ) {
        // 메인 버블
        drawCircle(
            color = bubble.color,
            radius = size.width / 2,
            center = center
        )

        // 버블 타입별 표시
        when (bubble.type) {
            BubbleType.BOMB -> {
                // 폭탄 표시 (작은 검은 원)
                drawCircle(
                    color = Color.Black,
                    radius = size.width / 6,
                    center = center
                )
            }
            BubbleType.BONUS -> {
                // 보너스 표시 (작은 흰색 원)
                drawCircle(
                    color = Color.White,
                    radius = size.width / 6,
                    center = center
                )
            }
            BubbleType.NORMAL -> {
                // 일반 버블은 추가 표시 없음
            }
        }
    }
}

//상단 점수 & 시간 표시
@Composable
fun GameStatusRow(score: Int, timeLeft: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Score: $score", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(text = "Time: ${timeLeft}s", fontSize = 24.sp, fontWeight = FontWeight.Bold)
    }
}