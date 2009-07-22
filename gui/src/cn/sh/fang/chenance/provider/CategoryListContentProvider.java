/*
 * Copyright 2008 Wencheng FANG
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
package cn.sh.fang.chenance.provider;

import static cn.sh.fang.chenance.i18n.UIMessageBundle._;

import java.util.List;

import cn.sh.fang.chenance.ChenanceDataException;
import cn.sh.fang.chenance.data.dao.CategoryService;
import cn.sh.fang.chenance.data.entity.Category;

public class CategoryListContentProvider extends BaseProvider<Category> {

	private Category root;

	List<Category> tops;
	
	List<Category> all;

	CategoryService cs = new CategoryService();

	public CategoryListContentProvider() {
		this.initData();
	}

	private void initData() {
		tops = cs.getTops();
		this.root = new Category();
		root.setChildren(tops);
		all = cs.findAll();
	}

	public Category getRoot() {
		return root;
	}

	public static Integer generateCode(Category parent) throws ChenanceDataException {
		int pid = parent.getCode();
		int i = parent.getChildren().size() + 1;
		if (i >= 100) {
			throw new ChenanceDataException();
		}
		if (pid % 1000000 == 0) {
			return pid + i * 10000;
		} else if (pid % 10000 == 0) {
			return pid + i * 100;
		} else {
			throw new ChenanceDataException();
		}
	}

	@Override
	protected Category doAddItem() {
		/*
		int code = 0;
		try {
			code = generateCode(parent);
		} catch (ChenanceDataException e1) {
			// impossible
		}
		*/

		Category c = new Category();
		//c.setCode(code);
		c.setCode(0);
		//c.setParent(parent);
		c.setName(_("New Category"));
		c.setDescription(_("Description of New Category"));
		c.setUpdater("USER");
		cs.save(c);
		
		all.add(c);
		
		return c;
	}

	@Override
	protected Category doRemoveItem(Category t) {
		cs.remove(t.getId(), "USER");
		all.remove(t);
		return null;
	}

	@Override
	protected Category doUpdateItem(Category t) {
		cs.save(t);
		return t;
	}

	public List<Category> getAll() {
		return all;
	}

}
