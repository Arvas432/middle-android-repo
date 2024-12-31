package com.example.androidpracticumcustomview.ui.theme

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/*
Задание:
Реализуйте необходимые компоненты;
Создайте проверку что дочерних элементов не более 2-х;
Предусмотрите обработку ошибок рендера дочерних элементов.
Задание по желанию:
Предусмотрите параметризацию длительности анимации.
 */
@Composable
fun CustomContainerCompose(
    firstChild: @Composable (() -> Unit)?,
    secondChild: @Composable (() -> Unit)?
) {
    var firstElementSize by remember { mutableStateOf(IntSize.Zero) }
    var secondElementSize by remember { mutableStateOf(IntSize.Zero) }
    var maxHeightPx by remember { mutableStateOf(0f) }
    val firstPositionY = remember { Animatable(0f) }
    val secondPositionY = remember { Animatable(0f) }
    val firstAlpha = remember { Animatable(0f) }
    val secondAlpha = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            firstPositionY.snapTo(maxHeightPx / 2f)
            secondPositionY.snapTo(maxHeightPx / 2f)
        }
        if (firstChild != null) {
            println("Initial firstPositionY: ${firstPositionY.value}, Alpha: ${firstAlpha.value}")
            scope.launch {
                firstPositionY.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(durationMillis = 5000, easing = EaseInOut)
                )
            }
            scope.launch {
                firstAlpha.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(durationMillis = 3000)
                )
            }
        }
        if (secondChild != null) {
            delay(1000)
            println("Initial secondPositionY: ${secondPositionY.value}, secondAlpha: ${secondAlpha.value}")

            scope.launch {
                secondPositionY.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(durationMillis = 5000, easing = EaseInOut)
                )
                println("Final secondPositionY: ${secondPositionY.value}")
            }
            scope.launch {
                secondAlpha.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(durationMillis = 3000)
                )
                println("Final secondAlpha: ${secondAlpha.value}")
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onGloballyPositioned { coordinates ->
                maxHeightPx = coordinates.size.height.toFloat()
            }
    ) {
        firstChild?.let {
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset { IntOffset(0, firstPositionY.value.toInt()) }
                    .graphicsLayer(alpha = firstAlpha.value)
                    .onGloballyPositioned { layoutCoordinates ->
                        firstElementSize = layoutCoordinates.size
                    }
            ) {
                it()
            }
        }
        secondChild?.let {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset { IntOffset(0, -secondPositionY.value.toInt()) }
                    .graphicsLayer(alpha = secondAlpha.value)
                    .onGloballyPositioned { layoutCoordinates ->
                        secondElementSize = layoutCoordinates.size
                    }
            ) {
                it()
            }
        }
    }
}
