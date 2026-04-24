import { type Ref, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import { type IEspecialidad } from '@/shared/model/especialidad.model';

import EspecialidadService from './especialidad.service';

export default defineComponent({
  name: 'EspecialidadDetails',
  setup() {
    const especialidadService = inject('especialidadService', () => new EspecialidadService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const especialidad: Ref<IEspecialidad> = ref({});

    const retrieveEspecialidad = async especialidadId => {
      try {
        const res = await especialidadService().find(especialidadId);
        especialidad.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.especialidadId) {
      retrieveEspecialidad(route.params.especialidadId);
    }

    return {
      alertService,
      especialidad,

      previousState,
    };
  },
});
