package com.example.jummechu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class LunchMenu(val name: String, val icon: String)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFF5F5F5) // 배경색을 아주 연한 회색으로 설정
                ) {
                    LunchRouletteScreen()
                }
            }
        }
    }
}

@Composable
fun LunchRouletteScreen() {
    // 메뉴 데이터 준비 (음식 이름 + 이모지)
    val menuList = listOf(
        LunchMenu("김치찌개", "🥘"),
        LunchMenu("돈까스", "🍱"),
        LunchMenu("짜장면", "🍜"),
        LunchMenu("햄버거", "🍔"),
        LunchMenu("초밥", "🍣"),
        LunchMenu("피자", "🍕"),
        LunchMenu("삼겹살", "🥓"),
        LunchMenu("치킨", "🍗"),
        LunchMenu("샐러드", "🥗"),
        LunchMenu("떡볶이", "🌶️")
    )

    // 현재 선택된 메뉴
    var currentMenu by remember { mutableStateOf(LunchMenu("버튼을 눌러봐!", "❓")) }
    // 룰렛이 돌아가는 중인지 확인 (중복 클릭 방지)
    var isRolling by remember { mutableStateOf(false) }
    // 코루틴 스코프 (애니메이션/시간 지연을 위해 필요)
    val coroutineScope = rememberCoroutineScope()

    //화면 UI
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "오늘 점심 뭐 먹지?",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(30.dp))

        // 메인 카드 (결과가 나오는 곳)
        Card(
            modifier = Modifier
                .size(300.dp) // 카드 크기
                .shadow(elevation = 10.dp, shape = RoundedCornerShape(20.dp)), // 그림자 효과
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // 음식 아이콘 (크게)
                Text(text = currentMenu.icon, fontSize = 100.sp)

                Spacer(modifier = Modifier.height(20.dp))

                // 음식 이름
                Text(
                    text = currentMenu.name,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.Black
                )
            }
        }

        Spacer(modifier = Modifier.height(50.dp))

        // 추천 버튼
        Button(
            onClick = {
                // 룰렛 로직 시작
                if (!isRolling) {
                    isRolling = true // 버튼 비활성화 (중복 클릭 방지)

                    coroutineScope.launch {
                        // 1단계: 빠르게 바뀌는 효과 (20번 반복)
                        repeat(20) {
                            currentMenu = menuList.random()
                            delay(50) // 0.05초 대기
                        }
                        // 2단계: 조금 느리게 (5번 반복)
                        repeat(5) {
                            currentMenu = menuList.random()
                            delay(150) // 0.15초 대기
                        }
                        // 3단계: 최종 결과 확정
                        currentMenu = menuList.random()
                        isRolling = false // 버튼 다시 활성화
                    }
                }
            },
            enabled = !isRolling, // 돌아가는 중에는 버튼 못 누르게 막음
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE)),
            modifier = Modifier
                .width(250.dp)
                .height(60.dp)
        ) {
            Text(
                text = if (isRolling) "메뉴 고르는 중..." else "랜덤 추천 시작! 🎲",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}