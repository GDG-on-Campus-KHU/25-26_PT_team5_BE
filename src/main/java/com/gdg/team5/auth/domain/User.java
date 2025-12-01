package com.gdg.team5.auth.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.experimental.FieldDefaults; // @FieldDefaults 임포트
import lombok.AccessLevel;
import org.springframework.data.annotation.Immutable;
import org.springframework.data.annotation.PersistenceConstructor;

// @Builder를 record에 직접 적용하고, 필요한 생성자를 명시적으로 정의합니다.
// @FieldDefaults(level = AccessLevel.PRIVATE) // record는 필드가 이미 private final이므로 불필요
@Entity
@Immutable
@Builder(toBuilder = true) // toBuilder = true를 통해 새 record 반환 기능 지원
public record User(
        
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(nullable = false, unique = true)
        // 1. @Builder.Default를 사용하여 id를 Builder 패턴에서 선택적으로 만들고 null로 기본값 설정
        @Builder.Default
        Long id, 
        
        @Column(nullable = false, unique = true)
        String email, 
        
        @Column(nullable = false)
        String password, 
        
        @Column(nullable = false)
        String name
) {
    
    // 1. **@PersistenceConstructor를 위한 명시적 구현 (DB 로딩용)**
    // JPA가 DB에서 모든 필드를 읽어올 때 이 생성자를 사용합니다.
    @PersistenceConstructor
    public User(Long id, String email, String password, String name) {
        // record는 이 정규 생성자 내에서 필드 할당을 자동으로 처리합니다.
        // 유효성 검사 외의 로직은 필요 없습니다.
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
    }

    // 2. 새로운 record를 생성하는 builder 패턴을 위한 편의 생성자 (ID 제외)
    // DB에 저장할 때 사용하며, ID는 null로 시작합니다.
    // 이 생성자는 자동으로 위의 @Builder.Default 설정을 따라 id=null로 정규 생성자를 호출합니다.
    
    // 3. 엔티티 수정 로직 (Record는 불변이므로, 새 Record를 반환)
    public User updatePassword(String newPassword) {
        // toBuilder()를 사용하여 새로운 Builder를 얻고, 원하는 필드만 변경 후 build() 합니다.
        // toBuilder = true 설정이 필요합니다.
        return this.toBuilder().password(newPassword).build();
    }
    
    public User updateName(String newName) {
        return this.toBuilder().name(newName).build();
    }
}