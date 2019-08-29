package khairy.com.jakegithub.resource;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Resource<T> {


    @NonNull
    public final AuthStatus status;

    @Nullable
    public final T data;

    @Nullable
    public final String message;

    @Nullable
    public final Boolean isConnected;

    @Nullable
    public final Boolean isEnded;


    public Resource(@NonNull AuthStatus status, @Nullable T data, @Nullable String message, @Nullable Boolean isConnected, @Nullable Boolean isEnded) {
        this.status = status;
        this.data = data;
        this.message = message;
        this.isConnected = isConnected;
        this.isEnded = isEnded;
    }

    public static <T> Resource<T> success(@Nullable T data, Boolean isConnected, Boolean isEnded) {
        return new Resource<>(AuthStatus.SUCCESS, data, null, isConnected, isEnded);
    }

    public static <T> Resource<T> error(@NonNull String msg, @Nullable T data) {
        return new Resource<>(AuthStatus.ERROR, data, msg, false, true);
    }

    public static <T> Resource<T> loading(@Nullable T data, Boolean isConnected) {
        return new Resource<>(AuthStatus.LOADING, data, null, isConnected, false);
    }


    public enum AuthStatus {SUCCESS, ERROR, LOADING}

}
