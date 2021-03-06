/*
 * SonarQube
 * Copyright (C) 2009-2017 SonarSource SA
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.server.platform.db.migration.version.v61;

import java.sql.SQLException;
import java.sql.Types;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.sonar.api.utils.System2;
import org.sonar.db.DbTester;

import static org.assertj.core.api.Assertions.assertThat;

public class CreateTableQprofileChangesTest {
  private static final String TABLE_QPROFILE_CHANGES = "qprofile_changes";

  @Rule
  public final DbTester dbTester = DbTester.createForSchema(System2.INSTANCE, CreateTableQprofileChangesTest.class, "empty.sql");
  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  private CreateTableQprofileChanges underTest = new CreateTableQprofileChanges(dbTester.database());

  @Test
  public void creates_table_on_empty_db() throws SQLException {
    underTest.execute();

    assertThat(dbTester.countRowsOfTable(TABLE_QPROFILE_CHANGES)).isEqualTo(0);

    dbTester.assertColumnDefinition(TABLE_QPROFILE_CHANGES, "kee", Types.VARCHAR, 40, false);
    dbTester.assertColumnDefinition(TABLE_QPROFILE_CHANGES, "qprofile_key", Types.VARCHAR, 255, false);
    dbTester.assertColumnDefinition(TABLE_QPROFILE_CHANGES, "change_type", Types.VARCHAR, 20, false);
    dbTester.assertColumnDefinition(TABLE_QPROFILE_CHANGES, "user_login", Types.VARCHAR, 255, true);
    dbTester.assertColumnDefinition(TABLE_QPROFILE_CHANGES, "change_data", Types.CLOB, null, true);
    dbTester.assertColumnDefinition(TABLE_QPROFILE_CHANGES, "created_at", Types.BIGINT, null, false);
    dbTester.assertPrimaryKey(TABLE_QPROFILE_CHANGES, "pk_" + TABLE_QPROFILE_CHANGES, "kee");
  }

  @Test
  public void migration_is_not_reentrant() throws SQLException {
    underTest.execute();

    expectedException.expect(IllegalStateException.class);

    underTest.execute();
  }
}
