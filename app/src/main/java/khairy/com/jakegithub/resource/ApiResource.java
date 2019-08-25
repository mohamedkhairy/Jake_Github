package khairy.com.jakegithub.resource;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ApiResource<T> {


    @NonNull
    public final AuthStatus status;

    @Nullable
    public final T data;

    @Nullable
    public final String message;

    @Nullable
    public final Boolean isEnd;


    public ApiResource(@NonNull AuthStatus status, @Nullable T data, @Nullable String message, @Nullable Boolean isEnd) {
        this.status = status;
        this.data = data;
        this.message = message;
        this.isEnd = isEnd;
    }

    public static <T> ApiResource<T> success(@Nullable T data, Boolean isEnd) {
        return new ApiResource<>(AuthStatus.SUCCESS, data, null, isEnd);
    }

    public static <T> ApiResource<T> error(@NonNull String msg, @Nullable T data) {
        return new ApiResource<>(AuthStatus.ERROR, data, msg, true);
    }

    public static <T> ApiResource<T> loading(@Nullable T data) {
        return new ApiResource<>(AuthStatus.LOADING, data, null, true);
    }

    public enum AuthStatus {SUCCESS, ERROR, LOADING}

}
