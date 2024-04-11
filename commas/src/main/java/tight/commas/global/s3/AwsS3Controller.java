//package tight.commas.global.s3;
//
//import com.amazonaws.services.s3.AmazonS3Client;
//import com.amazonaws.services.s3.model.ObjectMetadata;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/file")
//@RequiredArgsConstructor
//public class AwsS3Controller {
//
//    private final AwsS3Service awsS3Service;
//
//    @PostMapping
//    public ResponseEntity<List<String>> uploadFile(@RequestParam("file") List<MultipartFile> files) {
//        return awsS3Service.uploadFile(files);
//    }
//}
