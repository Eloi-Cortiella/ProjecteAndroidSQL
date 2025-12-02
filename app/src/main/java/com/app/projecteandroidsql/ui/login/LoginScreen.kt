package com.app.projecteandroidsql.ui.login

import android.widget.Space
import com.app.projecteandroidsql.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.liveData
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreen() {
    Box(
        Modifier
            .fillMaxSize()
            .padding(16.dp)) {
        Login(Modifier.align (Alignment.Center))
    }
}

@Composable
fun Login(modifier: Modifier) {
    Column(modifier = modifier) {
        HeaderImatge()
        Spacer(modifier = Modifier.padding(16.dp))
        EmailField()
        Spacer(modifier = Modifier.padding(16.dp))
        PasswordField()
        Spacer(modifier = Modifier.padding(16.dp))
        //ForgotPassword()
        Spacer(modifier = Modifier.padding(16.dp))
        LoginButton()
    }
}

@Composable
fun ForgotPassword() {
    Text(
        text = stringResource(id = R.string.forgot_password),
        color = Color.Blue,
        modifier = Modifier.align(Alignment.End)
    ))
}

@Composable
fun EmailField() {
    TextField(value = "", onValueChange= {},
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(text = "Email") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        singleLine = true,
        maxLines = 1
    )
}


@Composable
fun PasswordField() {
    TextField(value = "", onValueChange= {},
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(text = "Password") },
        visualTransformation = PasswordVisualTransformation(),
        singleLine = true,
        maxLines = 1
    )
}

@Composable
fun LoginButton() {
    Button(
        onClick = { /* Handle login action */ },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = "Login")
    }
}

@Composable
fun HeaderImatge() {
    Image(
        painter = painterResource(id = R.drawable.headerimatgeprova),
        contentDescription = "Header",
        modifier = Modifier.fillMaxWidth()
    )
}