plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    `maven-publish`
}
gradlePlugin{
    plugins{
        create("autoExcludeActivityFrom"){
            group = "me.parade.gradle"
            version = "1.0.0"
            id = "me.parade.dependency-exclude"
            implementationClass = "me.parade.plugin.DependencyExclusionPlugin"
            displayName = "Auto Exclude Plugin"
            description = "Automatically excludes xx from xx"
        }
    }
}

publishing{
   repositories{
       maven {
           url = uri("../local_maven")
       }
   }
}