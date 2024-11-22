package com.tyris.annotatedstring

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.annotatedstring.R
import com.tyris.annotatedstring.ui.theme.ComposablesTyrisTheme
import com.tyris.annotatedstring.ui.theme.Poppins

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposablesTyrisTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AnnotatedView(
                        text = stringResource(id = R.string.dont_have_an_account) + " ",
                        clicableText = stringResource(id = R.string.sign_up),
                        modifier = Modifier.padding(innerPadding),
                        textStyle = SpanStyle(
                            fontFamily = Poppins,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        ),
                        clicableStyle = SpanStyle(
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary,
                            fontFamily = Poppins
                        ),
                        itemClicked = {
                            Log.d("AnnotatedString", "Clicked on: ${it}")
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun AnnotatedView(
    text: String,
    clicableText: String,
    itemClicked: (String) -> Unit,
    modifier: Modifier = Modifier,
    textStyle: SpanStyle = SpanStyle(),
    clicableStyle: SpanStyle = SpanStyle()
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(vertical = 32.dp)
            .padding(top = 16.dp)
    ) {
        val annotatedString = buildAnnotatedString {
            withStyle(
                style = textStyle
            ) {
                append(text)
                pushStringAnnotation(
                    tag = "clickable_text",
                    annotation = clicableText
                )
                withStyle(
                    style = clicableStyle
                ) {
                    append(clicableText)
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentAlignment = Alignment.BottomCenter
        ){
            ClickableText(
                text = annotatedString,
                onClick = { offset ->
                    annotatedString.getStringAnnotations(
                        tag = "clickable_text",
                        start = offset,
                        end = offset
                    ).firstOrNull()?.let {
                        itemClicked(it.item)
                    }
                }
            )
        }
    }
}
