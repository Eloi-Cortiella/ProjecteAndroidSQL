package com.app.projecteandroidsql.ui.login

import com.app.projecteandroidsql.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(viewModel: LoginViewModel) {
    Box(
        Modifier
            .fillMaxSize()
            .padding(16.dp)) {
        Login(Modifier.align(Alignment.Center), viewModel)
    }
}

@Composable
fun Login(modifier: Modifier, viewModel: LoginViewModel) {

    val email:String by viewModel.email.observeAsState("")
    val password:String by viewModel.password.observeAsState("")
    val loginEnable:Boolean by viewModel.loginEnable.observeAsState(false)
    val isLoading:Boolean by viewModel.isLoading.observeAsState(false)
    val coroutineScope = rememberCoroutineScope()

    // Moment de carrega si s'ha iniciat la funcio de login
    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }
    else {
        Column(modifier = modifier) {
            HeaderImatge()
            Spacer(modifier = Modifier.padding(16.dp))
            EmailField(email) {viewModel.onLoginChanged(it, password)}
            Spacer(modifier = Modifier.padding(4.dp))
            PasswordField(password) {viewModel.onLoginChanged(email, it)}
            Spacer(modifier = Modifier.padding(8.dp))
            ForgotPassword(Modifier.align(Alignment.Start))
            Spacer(modifier = Modifier.padding(16.dp))
            LoginButton(loginEnable) {
                coroutineScope.launch {
                    viewModel.onLoginSelected()
                }
            }
        }
    }
}



@Composable
fun EmailField(email:String, onTextFieldChanged:(String) -> Unit) {
    TextField(value = email, {onTextFieldChanged(it)},
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(text = "Email") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        singleLine = true,
        maxLines = 1
    )
}


@Composable
fun PasswordField(password:String, onTextFieldChanged:(String) -> Unit) {
    TextField(value = password, {onTextFieldChanged(it)},
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(text = "Password") },
        visualTransformation = PasswordVisualTransformation(),
        singleLine = true,
        maxLines = 1,
    )
}

@Composable
fun ForgotPassword(modifier: Modifier) {
    Text(
        text = stringResource(id = R.string.forgot_password),
        modifier = Modifier.clickable { },
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Black
    )
}

@Composable
fun LoginButton(loginEnable: Boolean, onLoginSelected: () -> Unit) {
    Button(
        onClick = { onLoginSelected() },
        modifier = Modifier.fillMaxWidth().height(48.dp),
        colors = ButtonDefaults.buttonColors(contentColor = Color.White,
            disabledContainerColor = Color.Gray,
            disabledContentColor = Color.Black
        ), enabled = loginEnable
    ) {
        Text(text = "Iniciar Sessió")
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