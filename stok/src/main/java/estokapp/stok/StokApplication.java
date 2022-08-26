package estokapp.stok;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages={"estokapp.stok"})
@SpringBootApplication
public class StokApplication {

	public static void main(String[] args) {
		SpringApplication.run(StokApplication.class, args);
	}

}
