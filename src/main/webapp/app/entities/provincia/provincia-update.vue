<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="hospitalApp.provincia.home.createOrEditLabel" data-cy="ProvinciaCreateUpdateHeading">Crear o editar Provincia</h2>
        <div>
          <div class="mb-3" v-if="provincia.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="provincia.id" readonly />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="provincia">Nombre</label>
            <input
              type="text"
              class="form-control"
              name="nombre"
              id="provincia-nombre"
              data-cy="nombre"
              :class="{ valid: !v$.nombre.$invalid, invalid: v$.nombre.$invalid }"
              v-model="v$.nombre.$model"
              required
            />
            <div v-if="v$.nombre.$anyDirty && v$.nombre.$invalid">
              <small class="form-text text-danger" v-for="error of v$.nombre.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="provincia">Codigo</label>
            <input
              type="text"
              class="form-control"
              name="codigo"
              id="provincia-codigo"
              data-cy="codigo"
              :class="{ valid: !v$.codigo.$invalid, invalid: v$.codigo.$invalid }"
              v-model="v$.codigo.$model"
            />
            <div v-if="v$.codigo.$anyDirty && v$.codigo.$invalid">
              <small class="form-text text-danger" v-for="error of v$.codigo.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="provincia">Fecha Alta</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="provincia-fechaAlta"
                  v-model="v$.fechaAlta.$model"
                  name="fechaAlta"
                  class="form-control"
                  :locale="currentLanguage"
                  button-only
                  today-button
                  reset-button
                  close-button
                >
                </b-form-datepicker>
              </b-input-group-prepend>
              <b-form-input
                id="provincia-fechaAlta"
                data-cy="fechaAlta"
                type="text"
                class="form-control"
                name="fechaAlta"
                :class="{ valid: !v$.fechaAlta.$invalid, invalid: v$.fechaAlta.$invalid }"
                v-model="v$.fechaAlta.$model"
                required
              />
            </b-input-group>
            <div v-if="v$.fechaAlta.$anyDirty && v$.fechaAlta.$invalid">
              <small class="form-text text-danger" v-for="error of v$.fechaAlta.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="provincia">Fecha Baja</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="provincia-fechaBaja"
                  v-model="v$.fechaBaja.$model"
                  name="fechaBaja"
                  class="form-control"
                  :locale="currentLanguage"
                  button-only
                  today-button
                  reset-button
                  close-button
                >
                </b-form-datepicker>
              </b-input-group-prepend>
              <b-form-input
                id="provincia-fechaBaja"
                data-cy="fechaBaja"
                type="text"
                class="form-control"
                name="fechaBaja"
                :class="{ valid: !v$.fechaBaja.$invalid, invalid: v$.fechaBaja.$invalid }"
                v-model="v$.fechaBaja.$model"
              />
            </b-input-group>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="provincia">Activo</label>
            <input
              type="checkbox"
              class="form-check"
              name="activo"
              id="provincia-activo"
              data-cy="activo"
              :class="{ valid: !v$.activo.$invalid, invalid: v$.activo.$invalid }"
              v-model="v$.activo.$model"
              required
            />
            <div v-if="v$.activo.$anyDirty && v$.activo.$invalid">
              <small class="form-text text-danger" v-for="error of v$.activo.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="provincia">Pais</label>
            <select class="form-control" id="provincia-pais" data-cy="pais" name="pais" v-model="provincia.pais" required>
              <option v-if="!provincia.pais" :value="null" selected></option>
              <option
                :value="provincia.pais && paisOption.id === provincia.pais.id ? provincia.pais : paisOption"
                v-for="paisOption in paises"
                :key="paisOption.id"
              >
                {{ paisOption.id }}
              </option>
            </select>
          </div>
          <div v-if="v$.pais.$anyDirty && v$.pais.$invalid">
            <small class="form-text text-danger" v-for="error of v$.pais.$errors" :key="error.$uid">{{ error.$message }}</small>
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" @click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span>Cancelar</span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="v$.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>Guardar</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./provincia-update.component.ts"></script>
