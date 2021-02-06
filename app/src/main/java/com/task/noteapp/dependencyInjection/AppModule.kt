package com.task.noteapp.dependencyInjection

import android.content.Context
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.task.noteapp.R
import com.task.noteapp.api.RetrofitAPI
import com.task.noteapp.repo.NoteRepository
import com.task.noteapp.repo.NoteRepositoryInterface
import com.task.noteapp.roomdb.NoteDao
import com.task.noteapp.roomdb.NoteDatabase
import com.task.noteapp.util.Util
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun injectRoomDB(@ApplicationContext context: Context) = Room.databaseBuilder(
        context,
        NoteDatabase::class.java,
        "NoteDatabase").build()

    @Singleton
    @Provides
    fun injectDao(database: NoteDatabase) = database.noteDao()


    @Singleton
    @Provides
    fun injectRetrofitAPI (): RetrofitAPI{
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Util.BASE_URL)
            .build()
            .create(RetrofitAPI::class.java)
    }

    @Singleton
    @Provides
    fun injectNormalRepo(dao: NoteDao, api: RetrofitAPI) = NoteRepository(dao, api) as NoteRepositoryInterface

    @Singleton
    @Provides
    fun injectGlide(@ApplicationContext context: Context) = Glide.with(context)
        .setDefaultRequestOptions(
           RequestOptions().placeholder(R.drawable.ic_launcher_foreground)
               .error(R.drawable.ic_launcher_foreground)
        )

}