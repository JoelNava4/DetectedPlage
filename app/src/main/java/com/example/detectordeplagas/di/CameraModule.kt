package com.example.detectordeplagas.di

import android.content.Context
import com.example.detectordeplagas.presentation.camera.CameraRepositoryImpl
import com.example.detectordeplagas.presentation.camera.CameraXManager
import com.example.domain.camera.repository.CameraRepository
import com.example.domain.camera.usecase.CapturarFotoUseCase
import com.example.domain.camera.usecase.DetenerPreviewUseCase
import com.example.domain.camera.usecase.IniciarPreviewUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CameraModule {

    @Provides
    @Singleton
    fun provideCameraXManager(
        @ApplicationContext context: Context
    ): CameraXManager = CameraXManager(context)

    @Provides
    @Singleton
    fun provideCameraRepository(
        cameraXManager: CameraXManager
    ): CameraRepository = CameraRepositoryImpl(cameraXManager)

    @Provides
    fun provideCapturarFotoUseCase(
        repo: CameraRepository
    ) = CapturarFotoUseCase(repo)

    @Provides
    fun provideIniciarPreviewUseCase(
        repo: CameraRepository
    ) = IniciarPreviewUseCase(repo)

    @Provides
    fun provideDetenerPreviewUseCase(
        repo: CameraRepository
    ) = DetenerPreviewUseCase(repo)
}