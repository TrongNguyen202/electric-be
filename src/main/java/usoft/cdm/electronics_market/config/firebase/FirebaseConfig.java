//package usoft.cdm.electronics_market.config.firebase;
//
//import com.google.auth.oauth2.GoogleCredentials;
//import com.google.firebase.FirebaseApp;
//import com.google.firebase.FirebaseOptions;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Configuration;
//
//import javax.annotation.PostConstruct;
//import java.io.IOException;
//import java.io.InputStream;
//
//@Configuration
//@RequiredArgsConstructor
//public class FirebaseConfig  {
//    @PostConstruct
//    public void init() {
//
//        try {
//            InputStream inputStream = FirebaseConfig.class.getClassLoader().getResourceAsStream("cho-dien-may-firebase-adminsdk-2st8p-d4c84b00ce.json");
//            FirebaseOptions options = new FirebaseOptions.Builder().setCredentials(GoogleCredentials.fromStream(inputStream)).build();
//            FirebaseApp.initializeApp(options);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}
