<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="hospitalApp.obraSocial.home.createOrEditLabel" data-cy="ObraSocialCreateUpdateHeading">Crear o editar Obra Social</h2>
        <div>
          <div class="mb-3" v-if="obraSocial.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="obraSocial.id" readonly />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="obra-social">Codigo</label>
            <input
              type="text"
              class="form-control"
              name="codigo"
              id="obra-social-codigo"
              data-cy="codigo"
              :class="{ valid: !v$.codigo.$invalid, invalid: v$.codigo.$invalid }"
              v-model="v$.codigo.$model"
              required
            />
            <div v-if="v$.codigo.$anyDirty && v$.codigo.$invalid">
              <small class="form-text text-danger" v-for="error of v$.codigo.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="obra-social">Nombre</label>
            <input
              type="text"
              class="form-control"
              name="nombre"
              id="obra-social-nombre"
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
            <label class="form-control-label" for="obra-social">Telefono</label>
            <input
              type="text"
              class="form-control"
              name="telefono"
              id="obra-social-telefono"
              data-cy="telefono"
              :class="{ valid: !v$.telefono.$invalid, invalid: v$.telefono.$invalid }"
              v-model="v$.telefono.$model"
            />
            <div v-if="v$.telefono.$anyDirty && v$.telefono.$invalid">
              <small class="form-text text-danger" v-for="error of v$.telefono.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="obra-social">Email</label>
            <input
              type="text"
              class="form-control"
              name="email"
              id="obra-social-email"
              data-cy="email"
              :class="{ valid: !v$.email.$invalid, invalid: v$.email.$invalid }"
              v-model="v$.email.$model"
            />
            <div v-if="v$.email.$anyDirty && v$.email.$invalid">
              <small class="form-text text-danger" v-for="error of v$.email.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="obra-social">Direccion</label>
            <input
              type="text"
              class="form-control"
              name="direccion"
              id="obra-social-direccion"
              data-cy="direccion"
              :class="{ valid: !v$.direccion.$invalid, invalid: v$.direccion.$invalid }"
              v-model="v$.direccion.$model"
            />
            <div v-if="v$.direccion.$anyDirty && v$.direccion.$invalid">
              <small class="form-text text-danger" v-for="error of v$.direccion.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="obra-social">Activo</label>
            <input
              type="checkbox"
              class="form-check"
              name="activo"
              id="obra-social-activo"
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
            <label class="form-control-label" for="obra-social">Fecha Alta</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="obra-social-fechaAlta"
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
                id="obra-social-fechaAlta"
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
            <label class="form-control-label" for="obra-social">Fecha Baja</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="obra-social-fechaBaja"
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
                id="obra-social-fechaBaja"
                data-cy="fechaBaja"
                type="text"
                class="form-control"
                name="fechaBaja"
                :class="{ valid: !v$.fechaBaja.$invalid, invalid: v$.fechaBaja.$invalid }"
                v-model="v$.fechaBaja.$model"
              />
            </b-input-group>
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
<script lang="ts" src="./obra-social-update.component.ts"></script>
