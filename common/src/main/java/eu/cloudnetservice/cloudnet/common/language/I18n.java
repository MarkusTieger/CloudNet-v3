/*
 * Copyright 2019-2022 CloudNetService team & contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package eu.cloudnetservice.cloudnet.common.language;

import eu.cloudnetservice.cloudnet.common.log.LogManager;
import eu.cloudnetservice.cloudnet.common.log.Logger;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import lombok.NonNull;

/**
 * The LanguageManager is a static service, which handles messages from different languages, which are registered or
 * loaded there
 */
public final class I18n {

  private static final Logger LOGGER = LogManager.logger(I18n.class);
  private static final Map<String, Properties> LANGUAGE_CACHE = new ConcurrentHashMap<>();

  /**
   * The current language, which the getMessage() method will the message return
   */
  private static volatile String language;

  private I18n() {
    throw new UnsupportedOperationException();
  }

  public static void loadFromLanguageRegistryFile(@NonNull ClassLoader source) {
    try (var reader = new BufferedReader(new InputStreamReader(
      Objects.requireNonNull(source.getResourceAsStream("languages.txt")),
      StandardCharsets.UTF_8))
    ) {
      String lang;
      while ((lang = reader.readLine()) != null) {
        var stream = source.getResourceAsStream("lang/" + lang);
        if (stream != null) {
          addLanguageFile(lang.replace(".properties", ""), stream);
        } else {
          LOGGER.fine("Skipping default language %s because the file is missing", null, lang);
        }
      }
    } catch (IOException exception) {
      LOGGER.severe("Unable to load language registry", exception);
    }
  }

  /**
   * Resolve and returns the following message in the language which is currently set as member "language"
   *
   * @param messageKey the following message property, which should sort out
   * @return the message which is defined in language cache or a fallback message like {@code "<language LANGUAGE not
   * found>"} or {@code "<message property not found in LANGUAGE>"}
   */
  public static @NonNull String trans(@NonNull String messageKey) {
    if (language == null || !LANGUAGE_CACHE.containsKey(language)) {
      return "<language " + language + " not found>";
    }

    return LANGUAGE_CACHE.get(language)
      .getProperty(messageKey, "<message " + messageKey + " not found in language " + language + ">");
  }

  /**
   * Add a new language properties, which can resolve with getMessage() in the configured language.
   *
   * @param language   the language, which should append
   * @param properties the properties which will add in the language as parameter
   */
  public static void addLanguageFile(@NonNull String language, @NonNull Properties properties) {
    LANGUAGE_CACHE.computeIfAbsent(language, $ -> new Properties()).putAll(properties);
    LOGGER.fine("Registering language file %s with %d translations", null, language, properties.size());
  }

  /**
   * Add a new language properties, which can resolve with getMessage() in the configured language.
   *
   * @param language the language, which should append
   * @param file     the properties which will add in the language as parameter
   */
  public static void addLanguageFile(@NonNull String language, @NonNull Path file) {
    try (var inputStream = Files.newInputStream(file)) {
      addLanguageFile(language, inputStream);
    } catch (IOException exception) {
      LOGGER.severe("Exception while reading language file", exception);
    }
  }

  /**
   * Add a new language properties, which can resolve with getMessage() in the configured language.
   *
   * @param language    the language, which should append
   * @param inputStream the properties which will add in the language as parameter
   */
  public static void addLanguageFile(@NonNull String language, @NonNull InputStream inputStream) {
    try (Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
      addLanguageFile(language, reader);
    } catch (IOException exception) {
      LOGGER.severe("Exception while reading language file", exception);
    }
  }

  /**
   * Add a new language properties, which can resolve with getMessage() in the configured language.
   *
   * @param language the language, which should append
   * @param reader   the properties which will be added in the language as parameter
   */
  public static void addLanguageFile(@NonNull String language, @NonNull Reader reader) {
    var properties = new Properties();

    try {
      properties.load(reader);
    } catch (IOException exception) {
      LOGGER.severe("Exception while reading language file", exception);
    }

    addLanguageFile(language, properties);
  }

  public static @NonNull String language() {
    return I18n.language;
  }

  public static void language(@NonNull String language) {
    I18n.language = language;
  }
}
