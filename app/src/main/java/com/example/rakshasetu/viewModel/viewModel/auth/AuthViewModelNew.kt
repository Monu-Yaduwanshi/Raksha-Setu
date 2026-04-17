//package com.example.rakshasetu.viewModel.viewModel.auth
//
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.rakshasetu.data.models.User
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.launch
//
//class AuthViewModel : ViewModel() {
//    private val repository = AuthRepository()
//
//    private val _isLoading = MutableStateFlow(false)
//    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
//
//    private val _currentUser = MutableStateFlow<User?>(null)
//    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()
//
//    private val _errorMessage = MutableStateFlow<String?>(null)
//    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()
//
//    init {
//        loadCurrentUser()
//    }
//
//    private fun loadCurrentUser() {
//        viewModelScope.launch {
//            val firebaseUser = repository.getCurrentUser()
//            if (firebaseUser != null) {
//                val user = repository.getUserData(firebaseUser.uid)
//                _currentUser.value = user
//            }
//        }
//    }
//
//    fun signIn(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
//        viewModelScope.launch {
//            _isLoading.value = true
//            val result = repository.signIn(email, password)
//            result.onSuccess { firebaseUser ->
//                val user = repository.getUserData(firebaseUser.uid)
//                _currentUser.value = user
//                _isLoading.value = false
//                onResult(true, null)
//            }.onFailure { e ->
//                _errorMessage.value = e.message
//                _isLoading.value = false
//                onResult(false, e.message)
//            }
//        }
//    }
//
//    fun signUp(
//        fullName: String,
//        email: String,
//        password: String,
//        phone: String,
//        role: String,
//        societyId: String = "",
//        societyName: String = "",
//        flatNumber: String = "",
//        blockNumber: String = "",
//        createdBy: String = "",
//        onResult: (Boolean, String?) -> Unit
//    ) {
//        viewModelScope.launch {
//            _isLoading.value = true
//            val result = repository.signUp(
//                fullName, email, password, phone, role,
//                societyId, societyName, flatNumber, blockNumber, createdBy
//            )
//            result.onSuccess { userId ->
//                loadCurrentUser()
//                _isLoading.value = false
//                onResult(true, userId)
//            }.onFailure { e ->
//                _errorMessage.value = e.message
//                _isLoading.value = false
//                onResult(false, e.message)
//            }
//        }
//    }
//
//    fun signOut() {
//        repository.signOut()
//        _currentUser.value = null
//    }
//
//    fun getCurrentUserId(): String? = repository.getCurrentUser()?.uid
//
//    fun clearError() {
//        _errorMessage.value = null
//    }
//}