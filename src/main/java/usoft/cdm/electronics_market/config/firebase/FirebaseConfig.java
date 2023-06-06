package usoft.cdm.electronics_market.config.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;

@Configuration
@RequiredArgsConstructor
public class FirebaseConfig {
    @Bean
    public FirebaseAuth firebaseAuth() throws IOException {
        // Đọc tệp cấu hình Firebase
        InputStream serviceAccount = FirebaseConfig.class.getClassLoader().getResourceAsStream("cho-dien-may-firebase-adminsdk-2st8p-d4c84b00ce.json");
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        // Khởi tạo FirebaseApp
        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
        }

        // Trả về FirebaseAuth
        return FirebaseAuth.getInstance();
    }
}







