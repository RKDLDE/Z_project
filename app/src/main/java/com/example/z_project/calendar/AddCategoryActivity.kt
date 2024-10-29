package com.example.z_project.calendar

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.z_project.databinding.ActivityAddCategoryBinding
import com.google.firebase.firestore.FirebaseFirestore

class AddCategoryActivity : AppCompatActivity() {

    lateinit var binding: ActivityAddCategoryBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var selectedCategory : ColorEnum

    private var colorList = listOf(
        ColorEnum.YELLOW, ColorEnum.GREEN, ColorEnum.BLUE, ColorEnum.PINK,
        ColorEnum.ORANGE, ColorEnum.PURPLE, ColorEnum.RED, ColorEnum.GRAY,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // user CODE 가져오기
        sharedPreferences = getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE)
        val uniqueCode = sharedPreferences.getString("UNIQUE_CODE", null)


        // Firebase에서 이미 저장되어 있는 카테고리 리스트 불러오기
        fetchUsedColors("1") { usedColors ->
            // 카테고리 color RecyclerView 설정 & Adapter 연결
            setupRecyclerView(usedColors)
        }


        // 이전으로 이동
        binding.addCategoryExit.setOnClickListener {
            finish()
        }


//        // RecyclerView 설정 & Adapter 연결
//        binding.addCategoryListRv.layoutManager = GridLayoutManager(this, 4)
//        val colorAdapter = ColorAdapter(colorList) { selectedColor ->
//            Toast.makeText(this, "Selected: ${selectedColor.name}", Toast.LENGTH_SHORT).show()
//            selectedCategory = selectedColor
//        }
//        binding.addCategoryListRv.adapter = colorAdapter


        // 저장하기 버튼 클릭 리스너
        binding.addCategorySave.setOnClickListener {
            val authId = uniqueCode
            val name = binding.addCategoryNameContent.text.toString() // 카테고리 이름
            val color = selectedCategory // 선택한 카테고리의 색상

            // Category 객체 생성
            val category = Categories(authId, "", name, color.toString())

            // Firestore에 저장
            saveCategoryToFirestore(category)

            finish()
        }

    }

    // firebase 생성한 카테고리 데이터 업로드
    private fun saveCategoryToFirestore(category: Categories) {
        val db = FirebaseFirestore.getInstance()
        val categoriesCollection = db.collection("categories") // "categories"라는 컬렉션에 저장

        categoriesCollection.add(category)
            .addOnSuccessListener { documentReference ->
                Log.d("Firestore", "Category added with ID: ${documentReference.id}")

                // 생성된 문서 ID를 Categories에 설정
                category.categoryId = documentReference.id
                updateCategoryInFirestore(category)
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error adding event", e)
            }
    }
    // Categories을 Firestore에 업데이트하는 함수
    private fun updateCategoryInFirestore(category: Categories) {
        val db = FirebaseFirestore.getInstance()
        val eventsCollection = db.collection("categories")

        // documentId가 null이 아니면 업데이트 진행
        category.categoryId?.let { id ->
            eventsCollection.document(id).set(category)
                .addOnSuccessListener {
                    Log.d("Firestore", "category updated with ID: $id")
                }
                .addOnFailureListener { e ->
                    Log.w("Firestore", "Error updating category", e)
                }
        }
    }


    // firebase 에서 카테고리 데이터 받아오기
    private fun fetchUsedColors(groupId: String, onColorsFetched: (List<ColorEnum>) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        db.collection("categories")
            .whereEqualTo("groupId", groupId)
            .get()
            .addOnSuccessListener { result ->
                val usedColors = result.mapNotNull { document ->
                    val colorName = document.getString("color") // Assuming color is stored as a string name
                    ColorEnum.valueOf(colorName ?: "")
                }
                onColorsFetched(usedColors)
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error fetching colors", e)
                onColorsFetched(emptyList()) // If there's an error, return an empty list
            }
    }


    // 카테고리 Color RecyclerView 초기화
    private fun setupRecyclerView(usedColors: List<ColorEnum>) {
        // RecyclerView 설정 & Adapter 연결
        binding.addCategoryListRv.layoutManager = GridLayoutManager(this, 4)
        val colorAdapter = ColorAdapter(colorList, usedColors) { selectedColor ->
            Toast.makeText(this, "Selected: ${selectedColor.name}", Toast.LENGTH_SHORT).show()
            selectedCategory = selectedColor
        }
        binding.addCategoryListRv.adapter = colorAdapter
    }

}