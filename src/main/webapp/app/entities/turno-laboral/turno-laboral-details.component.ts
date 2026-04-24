import { type Ref, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import { type ITurnoLaboral } from '@/shared/model/turno-laboral.model';

import TurnoLaboralService from './turno-laboral.service';

export default defineComponent({
  name: 'TurnoLaboralDetails',
  setup() {
    const turnoLaboralService = inject('turnoLaboralService', () => new TurnoLaboralService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const turnoLaboral: Ref<ITurnoLaboral> = ref({});

    const retrieveTurnoLaboral = async turnoLaboralId => {
      try {
        const res = await turnoLaboralService().find(turnoLaboralId);
        turnoLaboral.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.turnoLaboralId) {
      retrieveTurnoLaboral(route.params.turnoLaboralId);
    }

    return {
      alertService,
      turnoLaboral,

      previousState,
    };
  },
});
